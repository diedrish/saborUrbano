
package controlador;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.swing.JOptionPane;


public class BACKUP {
  private int BUFFER = 10485760;  
    //para guardar en memmoria
    private StringBuffer temp = null;
    //para guardar el archivo SQL
    private FileWriter  fichero = null;
    private PrintWriter pw = null;
    
 public boolean CrearBackup(String host, String port, String user, String password, String db, String file_backup){
    boolean ok=false;
    try{       
        //sentencia para crear el BackUp
         Process run = Runtime.getRuntime().exec(
        "C:\\Program Files\\MySQL\\MySQL Server 5.6\\bin\\mysqldump --host=" + "localhost" + " --port=" + 3000 +
        " --user=" + "root" + " --password=" + "2520" +
        " --compact --complete-insert --extended-insert --skip-quote-names" +
        " --skip-comments --skip-triggers " + "puntoventa");
        //se guarda en memoria el backup
        InputStream in = run.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        temp = new StringBuffer();
        int count;
        char[] cbuf = new char[BUFFER];
        while ((count = br.read(cbuf, 0, BUFFER)) != -1)
            temp.append(cbuf, 0, count);
        br.close();
        in.close();        
        /* se crea y escribe el archivo SQL */
        fichero = new FileWriter(file_backup);
        pw = new PrintWriter(fichero);                                                    
        pw.println(temp.toString());  
        ok=true; 
        JOptionPane.showMessageDialog(null, "BackUp Creado con exito");
          
   }
    catch (Exception ex){
            ex.printStackTrace();
    } finally {
       try {           
         if (null != fichero)
              fichero.close();
       } catch (Exception e2) {
           e2.printStackTrace();
       }
    }   
    return ok; 
 }
}