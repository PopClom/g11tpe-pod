**g11tpe-pod**
**TP2 - PROGRAMACIÓN DE OBJETOS DISTRIBUIDOS** 


**Pre-requisitos**
Instalar maven

**Compilación**
1. Situarse en el directorio del proyecto
2. Ejecutar, desde la terminal:
  $> chmod u+x make.sh
  $>./make.sh
 
**Ejecución**
 
**-Server-**
Ejecutar en la terminal desde el directorio del proyecto:
    $> ./run-server -Dinterface=XXX.YYY.ZZZ.*
    
    Donde -Dinterface indica la interfaz que Hazelcast debería de usar
    
     Ejemplo de invocación:
        $> ./run-server -Dinterface=192.168.1.*
        
**-Client queries-**
Ejecutar en la terminal desde el directorio del proyecto:
    $> ./queryN -Daddresses="xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY" -DinPath=WW -DoutPath=ZZ [params]
    
     donde
             -queryN ​es el script que corre la query N.
             -Daddresses​ refiere a las direcciones IP de los nodos con sus puertos (una o más)
             -DinPath ​indica el path donde están ambos archivos de entrada ​aeropuertos.csv
             y ​movimientos.csv​.
             -DoutPath ​indica el path donde estarán ambos archivos de salida ​query1.csv y
             query1.txt​.
             [params]​: los parámetros extras para las queries 2 y 4:
                    -en la query 2, agregar el parámetro -Dn=N
                        $> ./queryN -Daddresses="=xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY -DinPath=WW -DoutPath=ZZ -Dn=N
                            donde N sirve para determinar las top N aerolineas según el porcentaje de movimeintos de cabotaje
                    
                    -en la query 4, agregar los parámetros -Dn=N y -Doaci=OACI
                             donde N sirve para determinar los n aeropuertos con mayro cantidad de movimientos de despegue que tienen como origen a un aeropuerto OACI.
               
       
      Ejemplo de invocación:
          $> ./query2 -Daddresses="192.168.1.10:5701;192.168.1.10:5702" -DoutPath=/home/maite/Documents/POD/TP2 -DinPath=/home/maite/Documents/POD/TP2 -Dn=3
      
                     