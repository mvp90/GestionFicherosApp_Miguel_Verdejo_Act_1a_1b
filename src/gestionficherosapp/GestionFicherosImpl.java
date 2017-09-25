package gestionficherosapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;

import gestionficheros.FormatoVistas;
import gestionficheros.GestionFicheros;
import gestionficheros.GestionFicherosException;
import gestionficheros.TipoOrden;

public class GestionFicherosImpl implements GestionFicheros {
	private File carpetaDeTrabajo = null;
	private Object[][] contenido;
	private int filas = 0;
	private int columnas = 3;
	private FormatoVistas formatoVistas = FormatoVistas.NOMBRES;
	private TipoOrden ordenado = TipoOrden.DESORDENADO;

	public GestionFicherosImpl() {
		carpetaDeTrabajo = File.listRoots()[0];
		actualiza();
	}

	private void actualiza() {

		String[] ficheros = carpetaDeTrabajo.list(); // obtener los nombres
		// calcular el número de filas necesario
		filas = ficheros.length / columnas;
		if (filas * columnas < ficheros.length) {
			filas++; // si hay resto necesitamos una fila más
		}

		// dimensionar la matriz contenido según los resultados

		contenido = new String[filas][columnas];
		// Rellenar contenido con los nombres obtenidos
		for (int i = 0; i < columnas; i++) {
			for (int j = 0; j < filas; j++) {
				int ind = j * columnas + i;
				if (ind < ficheros.length) {
					contenido[j][i] = ficheros[ind];
				} else {
					contenido[j][i] = "";
				}
			}
		}
	}

	@Override
	public void arriba() {

		System.out.println("holaaa");
		if (carpetaDeTrabajo.getParentFile() != null) {
			carpetaDeTrabajo = carpetaDeTrabajo.getParentFile();
			actualiza();
		}

	}

	@Override
	public void creaCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo,arg0);
		File folder = new File(carpetaDeTrabajo,"");
		
		//que ya exista -> lanzará una excepción
		if(file.exists()) {
			//Si el archivo no existe te indicará que no se puede crear porque ya existe.
			throw new GestionFicherosException("Error, el archivo con el nombre "+arg0+" ya existe."
					+ " Intentalo con otro nombre");
		}
			
		//que se pueda escribir -> lanzará una excepción
		if(!folder.canWrite()) {
			//Si no se tienen permisos para escribir en la ruta actual, mostrará mensaje de error.
			throw new GestionFicherosException("Error, no tienes permisos de escritura en " +carpetaDeTrabajo );
		}
		
			if(file.mkdirs()) {
				
				//Si el directorio se puede crear, mensaje de correcto.
				System.out.println("El directorio "+arg0+ " se ha creado correctamente.");
			}
			
			//Si el directorio NO se puede crear, mensaje de error.
				else {
					throw new GestionFicherosException("Error, el directorio con el nombre "+arg0+" NO se ha creado.");
			}

	actualiza();
	
	}
	

	@Override
	public void creaFichero(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		File file = new File(carpetaDeTrabajo,arg0);
			
			//que ya exista -> lanzará una excepción
			if(file.exists()) {
				//Si el archivo no existe te indicará que no se puede crear porque ya existe.
				throw new GestionFicherosException("Error, el fichero con el nombre "+arg0+" ya existe."
						+ " Intentalo con otro nombre");
			}
				
			//que se pueda escribir -> lanzará una excepción
			if(!carpetaDeTrabajo.canWrite()) {
				//Si no se tienen permisos para escribir en la ruta actual, mostrará mensaje de error.
				throw new GestionFicherosException("Error, no tienes permisos de escritura en " + carpetaDeTrabajo.getPath() );
			}
			
			
				try {
					file.createNewFile();
						
						//Si el fichero se puede crear, mensaje de correcto.
						System.out.println("El dfichero "+arg0+ " se ha creado correctamente.");
						
					
				} catch (IOException eo) {
					// TODO Auto-generated catch block
					throw new GestionFicherosException("No se ha creado el fichero "+arg0);
				}
			

		actualiza();
		}
		
	

	@Override
	public void elimina(String arg0) throws GestionFicherosException {
		
		File file = new File(carpetaDeTrabajo,arg0);
		
		//Comprobar permisos de modificacion
		if (!carpetaDeTrabajo.canWrite()) {
			throw new GestionFicherosException("NO tienes permisos para borrar en el directorio.");
		}
		
		//comprobar si existe el fichero
		if (!file.exists()) {
			throw new GestionFicherosException("NO existe el fichero "+arg0+" compruebalo.");
		}
		
		//Eliminar el archivo
		if (file.delete()) {
			System.out.println("Se ha eliminado el fichero "+arg0+" ¡BUENA SUERTE!");
		} else {
			throw new GestionFicherosException("No has podido borrar el fichero "+arg0+" . quizás ha sido mejor ¿NO?");
		}
		
	actualiza();
	}
		
		
		// TODO Auto-generated method stub

	

	@Override
	public void entraA(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);
		// se controla que el nombre corresponda a una carpeta existente
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se ha encontrado "
					+ file.getAbsolutePath()
					+ " pero se esperaba un directorio");
		}
		// se controla que se tengan permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException("Alerta. No se puede acceder a "
					+ file.getAbsolutePath() + ". No hay permiso");
		}
		// nueva asignación de la carpeta de trabajo
		carpetaDeTrabajo = file;
		// se requiere actualizar contenido
		actualiza();

	}

	@Override
	public int getColumnas() {
		return columnas;
	}

	@Override
	public Object[][] getContenido() {
		return contenido;
	}

	@Override
	public String getDireccionCarpeta() {
		return carpetaDeTrabajo.getAbsolutePath();
	}

	@Override
	public String getEspacioDisponibleCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEspacioTotalCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFilas() {
		return filas;
	}

	@Override
	public FormatoVistas getFormatoContenido() {
		return formatoVistas;
	}

	@Override
	public String getInformacion(String arg0) throws GestionFicherosException {
		
		StringBuilder strBuilder=new StringBuilder();
		File file = new File(carpetaDeTrabajo,arg0);
		
		//Controlar que existe. Si no, se lanzará una excepción
		//Controlar que haya permisos de lectura. Si no, se lanzará una excepción
		
		strBuilder.append("-Comprobación existencia archivo: ");
		strBuilder.append("\n");
		if(file.exists()) {
			//Si el archivo no existe que aparezca el mensaje de error.
			strBuilder.append("       El archivo existe");
			strBuilder.append("\n");
		
		}	else {
			
				throw new GestionFicherosException("       El archivo NO existe");
		}
		
		
		strBuilder.append("-Comprobación permisos de lectura: ");
		strBuilder.append("\n");
		if(file.canRead()) {
			//Si el archivo tiene permisos de lectura que aparezca el mensaje de confirmación.
			strBuilder.append("      El archivo tiene permisos de lectura");
			strBuilder.append("\n");
			//Si el archivo no tiene permisos de lectura que aparezca el mensaje de error.
		}	else {
			
				throw new GestionFicherosException("       El archivo NO tiene permisos de lectura");
		}
		
		
		
		//Título
		strBuilder.append("\n\n");
		strBuilder.append("INFORMACIÓN DEL SISTEMA");
		strBuilder.append("\n");
		strBuilder.append("-------------------------------------------------");
		strBuilder.append("\n\n");
		
		//Nombre
		strBuilder.append("=Nombre Archivo: ");
		strBuilder.append(arg0);
		strBuilder.append("\n");
		
		//Tipo: fichero o directorio
		//Si es directorio: Espacio libre, espacio disponible, espacio total
				//bytes
		
		if (file.isDirectory()) {
			strBuilder.append("-Tipo de archivo: Directorio");
			strBuilder.append("\n");
			
			//Declaracion list
			String[] arregloArchivos = file.list();
			//Paso a contar numero archivos
			int numArchivos = arregloArchivos.length;
			
			strBuilder.append("-Número de archivos: " + arregloArchivos.length);
			strBuilder.append("\n");
			
			strBuilder.append("-Espacio Libre: " + file.getFreeSpace() + " bytes");
			strBuilder.append("\n");
			
			strBuilder.append("-Espacio Disponible: " + file.getUsableSpace() + " bytes");
			strBuilder.append("\n");
			
			strBuilder.append("-Espacio Total: " + file.getTotalSpace() + " bytes");
			strBuilder.append("\n\n");
			
			} else {
				strBuilder.append("-Tipo de archivo: Fichero");
				strBuilder.append("\n");
				strBuilder.append("-Tamaño fichero: " + file.length() + " bytes");
				strBuilder.append("\n");
			}
		
		//Ubicación
		strBuilder.append("-Ruta: "  + file.getAbsolutePath());
		strBuilder.append("\n");
		
		//Fecha de última modificación
		strBuilder.append("-Fecha Ultima Modificacion: ");
		
		//Formateo de fecha y hora
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		strBuilder.append(sdf.format(file.lastModified()) +"\n");
		strBuilder.append("\n");
		
		
		//Si es un fichero oculto o no
		strBuilder.append("-El archivo está oculto (true/false):  "  + file.isHidden());
		strBuilder.append("\n");
		
		
		return strBuilder.toString();
		}
		
	
	@Override
	public boolean getMostrarOcultos() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNombreCarpeta() {
		return carpetaDeTrabajo.getName();
	}

	@Override
	public TipoOrden getOrdenado() {
		return ordenado;
	}

	@Override
	public String[] getTituloColumnas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getUltimaModificacion(String arg0)
			throws GestionFicherosException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String nomRaiz(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numRaices() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void renombra(String arg0, String arg1) throws GestionFicherosException {

	//DECLARAMOS EL FICHERO ORIGINAL
	File file = new File(carpetaDeTrabajo,arg0);

	//DECLARAMOS EL NUEVO FICHERO
	File NUEVOfile = new File(carpetaDeTrabajo,arg1);
	
	//COMPROBAMOS SI TENEMOS PERMISOS DE LECTURA
	if (!carpetaDeTrabajo.canRead()){
		throw new GestionFicherosException("No tienes permisos de lectura.");
	}
	
	//COMPROBAMOS SI EXISTE
	if (!file.exists()){
		throw new GestionFicherosException("No se puede renombrar algo que no existe :D");
	}
	if (NUEVOfile.exists()){
		throw new GestionFicherosException("No puedes usar ese nombre para renombrar, puesto que ya existe.");
	}
	
	//RENOMBRAMOS
	if (file.renameTo(NUEVOfile)){
		System.out.println("BYE BYE (se ha renombrado ) " + arg0 + " HOLA (a) " + arg1);
	} else {
		throw new GestionFicherosException("Error renombrando archivos");
	}
	
	
	actualiza();
}

	@Override
	public boolean sePuedeEjecutar(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeEscribir(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeLeer(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setColumnas(int arg0) {
		columnas = arg0;

	}

	@Override
	public void setDirCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(arg0);

		// se controla que la dirección exista y sea directorio
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se esperaba "
					+ "un directorio, pero " + file.getAbsolutePath()
					+ " no es un directorio.");
		}

		// se controla que haya permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException(
					"Alerta. No se puede acceder a  " + file.getAbsolutePath()
							+ ". No hay permisos");
		}

		// actualizar la carpeta de trabajo
		carpetaDeTrabajo = file;

		// actualizar el contenido
		actualiza();

	}

	@Override
	public void setFormatoContenido(FormatoVistas arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMostrarOcultos(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOrdenado(TipoOrden arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEjecutar(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEscribir(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeLeer(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUltimaModificacion(String arg0, long arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

}
