
package controlador;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class Path {
    
    public String FechaMostrar(Date fecha)
    {
        String NuevaFecha="";
        try
        { 
            String formato = "dd-MM-yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(formato);
            NuevaFecha = String.valueOf(sdf.format(fecha));
        } catch (Exception e){}
        return NuevaFecha;
    }
    
    public String Ruta(){
      
        return DatosSesion.ruta;
    }
    
    public String Hora(String hora)
    {
        String NuevaHora="";
        try
        {       
           String _24HourTime = hora;
           SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
           SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm");
           Date _24HourDt = _24HourSDF.parse(_24HourTime);
           NuevaHora=_12HourSDF.format(_24HourDt);
        } catch (Exception e){}
        return NuevaHora;
    }
    
    
    public String Fecha(Date fech)
    {
        String NuevaFecha="";
        try
        { 
            String formato = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(formato);
            NuevaFecha = String.valueOf(sdf.format(fech));
            NuevaFecha=NuevaFecha+" 00:00:00";
        } catch (Exception e){}
        return NuevaFecha;
    }
    
    public String FechaN(Date fech)
    {
        String NuevaFecha="";
        try
        { 
            String formato = "dd-MM-yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(formato);
            NuevaFecha = String.valueOf(sdf.format(fech));
        } catch (Exception e){}
        return NuevaFecha;
    }
}
