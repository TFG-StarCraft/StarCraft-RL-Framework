# TFG-StarCraft

## Herramienats externas

[BWAPI - Verión 4.1.2] (https://github.com/bwapi/bwapi) compatible con BWAPI Mirror

[BWAPI Mirror Versión 2.5] (https://github.com/vjurenka/BWMirror)

[JMathPlot] (https://github.com/yannrichet/jmathplot)

[JavaNativeAccess] (https://github.com/java-native-access/jna)

## Configuración del entorno x86

Con BWapi mirror hay que utilizar un entorno java x86, ya que el juego corre bajo x86

Propiedades -> Java Build Path -> (Libraries tab) -> Doble click en JRE System Library ->
Installed JREs -> Añadimos el JRE/JDK de x86 (que se encuentra por defecto en C:\Program Files (x86)\Java...)
	
Propiedades -> Java Build Path -> (Libraries tab) -> Doble click en JRE System Library -> 
Seleccionar Execution enviroment (primera opción) -> Enviroments... -> Java SE-1.8 -> seleccionar el que creamos antes
En el desplegable de execution enviroment tiene que aparecer el que acabamos de poner
	
## Configuración de los accesos directos

La posibilidad de cerrar el StarCraft desde la GUI de la aplicación se realiza mediante el
comando taskkill de windows. Sin embargo, para que funcione correctamente se requiere que 
se ejecute con permisos de administrador. Para conseguir ejecutar el comando con privilegios
de administrador desde java, se utiliza el exec de java para crear una nueva terminal, que 
ejecuta un acceso directo con privilegios de arministrador que apunta al .bat adjunto 
(que ejecuta taskkill sobre SC). Este acceso directo debe llamarse "forceClose.bat.lnk".

En cuanto a ejecutar el ChaosLauncher, el programa busca el acceso directo "Chaoslauncher.lnk"
que debe apuntar a Chaoslauncher.exe, se recomienda que lo haga con privilegios de administrador.

## Configurar BWAPI y el editor de mapas

Como configurar BWAPI y el editor de mapas para que las cosas funcionen (o al menos es 
lo que yo hice para conseguir que funcionara):

* En el editor:
	- Hagamos lo que hagamos, hay que poner una posición inicial para al menos dos jugadores,
	  aunque sólo usemos un jugador para, por ejemplo, un lebertinto.
	- En Jugador -> Ajustes, seleccionamos el jugador 2 (al que le hemos puesto la otra posición 
	  de inicio) y seleccionamos ordenador en el desplegable "Control" de arriba a la derecha.
	- En Escenario -> Facciones, movemos al jugador 2 a otra facción. Yo desmarqué todas las 
	  condiciones de victoria debajo de las listas.
	- En Escenario -> Iniciadores, borramos todos los iniciadores (para mí que eso en inglés
	  era triggers) de condiciones de victoria (para que no termine al instante al haber un
	  jugador sin unidades).
	  
* BWAPI (en el chaoslauncher, seleccionamos config con el BWAPI marcado)
	- map = DevMaps/Pruebas/MAPA.scm donde MAPA es el nombre del mapa. Como bien pone, la ruta
	  es relativa a la carpeta Starcraft. Por supuesto la jerarquía DevMaps/Pruebas/ es personalizable
	- game_type = USE_MAP_SETTINGS , para que deje usar el mapa con las opciones personalizadas de antes.
