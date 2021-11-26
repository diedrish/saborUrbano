package controlador;

import com.toedter.calendar.JDateChooser;
import dialog.SeleccionarImagen;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.TextField;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.channels.FileChannel;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author zamuria
 */
public class Metodos {

    DecimalFormatSymbols simbolos = DecimalFormatSymbols.getInstance(Locale.ENGLISH);
    DecimalFormat formateador = new DecimalFormat("####.##", simbolos);

    

    public String traerNumeroMovimiento(String id, String sucursal) {
        String numero = "", sql = "";

        sql = "select (actual+1)as actual from movimientos where idMovimiento='" + id + "' and idSucursal='" + sucursal + "'";

        numero = this.obtener_codigoDeCampo(sql, "actual");
        return numero;

    }

    //
    public String seleccionar_imagen(String documento, String variable, String dui) {
        String ruta = "";
        SeleccionarImagen si = new SeleccionarImagen(new javax.swing.JFrame(), true, dui, documento);
        setVisible(si, false);
        if (!si.ruta_seleccionada.equals("")) {
            ruta = si.ruta_copia;
        } else {
            if (si.eliminar) {
                ruta = "e";
            } else {
                ruta = variable;
            }
        }
        return ruta;
    }
////

    public boolean Instruccion_MySQL(String sql, String[] valores) {
        boolean exito = false;
        Connection cn2 = null;
        ResultSet rs2 = null;
        try {
            cn2 = Conexion.getConexion();
            CallableStatement st2 = cn2.prepareCall(sql);
            for (int i = 0; i < valores.length; i++) {
                st2.setString((i + 1), valores[i]);
            }
//            JOptionPane(st2.toString(), sql);
            st2.execute();
            exito = true;
            return exito;
        } catch (SQLException ex) {
            if (cn2 == null) {
                JOptionPane.showMessageDialog(null, "NO HAY CONEXION CON EL SERVIDOR");
            }

            try {
                if (cn2 != null) {
                    // cn2.close();
                }
            } catch (Exception e) {
            }
            return exito;
        } finally {
            try {
                if (cn2 != null) {
                    // cn2.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public void copiar_pegar_archivo(File origen1, File destino1) {
        if (destino1.exists()) {
            destino1.delete();
        }
        if (!destino1.exists()) {
            FileChannel origen = null;
            FileChannel destino = null;
            try {
                origen = new FileInputStream(origen1).getChannel();
                destino = new FileOutputStream(destino1).getChannel();

                long count = 0;
                long size = origen.size();
                while ((count += destino.transferFrom(origen, count, size - count)) < size);
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
            } finally {
                try {
                    if (origen != null) {
                        origen.close();
                    }
                    if (destino != null) {
                        destino.close();
                    }
                } catch (IOException ex) {
                }

            }
        }
    }

    public void setVisible(JDialog frame, boolean opcion) {
        if (opcion) {
            frame.setMaximumSize(new Dimension(810, 529));
            frame.setMinimumSize(new Dimension(810, 529));
            frame.setPreferredSize(new Dimension(810, 529));
        }
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public String seleccionar_imagen(JButton visualizar, String documento, String variable, String dui) {
        String ruta = "";
        SeleccionarImagen si = new SeleccionarImagen(new javax.swing.JFrame(), true, dui, documento);
        setVisible(si, false);
        if (!si.ruta_seleccionada.equals("")) {
            ruta = si.ruta_copia;
            visualizar.setEnabled(true);
        } else {
            if (si.eliminar) {
                visualizar.setEnabled(false);
                ruta = "e";
            } else {
                ruta = variable;
            }
        }
        return ruta;
    }

    public boolean eliminar_archivo(File ruta) {
        if (ruta.delete()) {
            return true;
        } else {
            return false;
        }
    }

    public String cambiar_ruta(String variable, String documento, String dui) {
        if (variable.equals("")) {
            return "";
        }
        String ruta = GenerarRutaAbsoluta("Documentos\\" + dui + "\\" + documento + "\\" + variable.replace(
                GenerarRutaAbsoluta("Copias\\" + dui + "\\" + documento + "\\"), "")
                .replace(GenerarRutaAbsoluta("Documentos\\" + dui + "\\" + documento + "\\"), ""));

        if (ruta.length() > 500) {
            ruta = "";
            System.out.println("Ruta demasiado grande");
            return ruta;
        }

        if (variable.equals("e")) {
            File[] files = new File(ruta.substring(0, ruta.length() - 2)).listFiles();
            try {
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            } catch (Exception e) {
            }
            eliminar_archivo(new File(ruta.substring(0, ruta.length() - 2)));
            return "";
        }
        CrearCarpeta("Documentos\\" + dui + "\\" + documento);
        if (!variable.equals(ruta)) {
            copiar_pegar_archivo(new File(variable), new File(ruta));
        }

        return ruta;

    }

///
    public String hora() {

        Calendar cal = Calendar.getInstance();
        String hora = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
        return hora;
    }
//////////

    public String FechaMostrar(Date fecha) {
        String NuevaFecha = "";
        try {
            String formato = "dd-MM-yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(formato);
            NuevaFecha = String.valueOf(sdf.format(fecha));
        } catch (Exception e) {
        }
        return NuevaFecha;
    }

    //
    public String obtener_codigoDeCampo(String sql, String campo_codigo) {
        Connection cn2 = null;
        Statement st2 = null;
        ResultSet rs2 = null;
        try {

            cn2 = Conexion.getConexion();
            st2 = cn2.createStatement();
            rs2 = st2.executeQuery(sql);
            while (rs2.next()) {
                return rs2.getString(campo_codigo);
            }
            return "";
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, ex.getMessage(), "Informacion", JOptionPane.INFORMATION_MESSAGE);
            return "";
        } finally {
            this.desconectar(st2, rs2, cn2);
        }
    }

    //llenar combobox
    public void llenarComboB_mysql(String sql, JComboBox cb, String campo_codigo, String campo_nombre) {
        Connection cn2 = null;
        Statement st2 = null;
        ResultSet rs2 = null;
        try {
            cn2 = Conexion.getConexion();
            st2 = cn2.createStatement();
            rs2 = st2.executeQuery(sql);
            cb.addItem("Seleccione");
            while (rs2.next()) {
                cb.addItem(rs2.getString(campo_codigo) + " |-| " + rs2.getString(campo_nombre));
            }
        } catch (SQLException ex) {
        } finally {
            this.desconectar(st2, rs2, cn2);
        }
    }

   

    public String Date_dMy_a_String_yMd(Date fech) {
        String NuevaFecha = "";
        try {
            String formato = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(formato);
            NuevaFecha = String.valueOf(sdf.format(fech));
        } catch (Exception e) {
        }
        return NuevaFecha.replace("/", "-");
    }

    public String mascara(double valor) {
        DecimalFormat df = new DecimalFormat("0.00");

        String val = valor + "";
        BigDecimal big = new BigDecimal(val);
        big = big.setScale(2, RoundingMode.HALF_UP);
        String resultado = df.format(big);
        
        return "" + resultado;
    }

    public double mascara2(double valor) {
        DecimalFormat df = new DecimalFormat("0.00");

        String val = valor + "";
        BigDecimal big = new BigDecimal(val);
        big = big.setScale(2, RoundingMode.HALF_UP);
        String resultado = df.format(big);
        return Double.parseDouble(resultado);
    }

  
    //validacion
    public boolean validacion(String Pregunta) {
        boolean procesar = false;
        int seleccion = JOptionPane.showOptionDialog(null, Pregunta, "Seleccione una opción", // Título
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Si", "No"}, "Si");
        if (seleccion != -1) {
            if ((seleccion + 1) == 1) {
                procesar = true;
            } else {
                procesar = false;
            }
        }
        return procesar;
    }

    //desconectar conexiones existentes
    public void desconectar(Statement st, ResultSet rs, Connection cn) {
        try {
            if (st != null) {
                st.close();
            }

            if (rs != null) {
                rs.close();
            }
////            if (cn != null) {
////                 //cn.close();
////            }

        } catch (SQLException ex) {
        }
    }

    //obtner codigo de un campo
    public String[] obtener_codigoDeCampo(String sql, String[] campo_codigo) {
        Connection cn2 = null;
        Statement st2 = null;
        ResultSet rs2 = null;
        String[] resultado = new String[campo_codigo.length];
        try {
            cn2 = Conexion.getConexion();
            st2 = cn2.createStatement();
            rs2 = st2.executeQuery(sql);
            while (rs2.next()) {
                for (int i = 0; i < campo_codigo.length; i++) {
                    resultado[i] = rs2.getString(campo_codigo[i]);
                }
            }
            return resultado;
        } catch (SQLException ex) {
            return resultado;
        } finally {
            this.desconectar(st2, rs2, cn2);
        }
    }

    //lenar tablas
    public JTable llenarTabla(String sql, String[] titulos, String[] campos) {
        Connection cn2 = null;
        Statement st2 = null;
        ResultSet rs2 = null;
        javax.swing.JTable jtable = new javax.swing.JTable();
        try {
            cn2 = Conexion.getConexion();
            st2 = cn2.createStatement();
            rs2 = st2.executeQuery(sql);
            jtable = this.ModeloTb_rSet(rs2, titulos, campos);
            return jtable;
        } catch (SQLException ex) {
            //this.JOptionPane(ex.getMessage(), sql);
            return jtable;
        } finally {
            this.desconectar(st2, rs2, cn2);
        }
    }

    public JTable ModeloTb_rSet(ResultSet rs, String[] titulos, String[] campos_mysql) {
        javax.swing.JTable jtable = new javax.swing.JTable();
        try {
            DefaultTableModel model = new DefaultTableModel(null, titulos);
            String[] Fila = new String[campos_mysql.length];
            while (rs.next()) {
                for (int i = 0; i < campos_mysql.length; i++) {
                    Fila[i] = rs.getString(campos_mysql[i]);
                }
                model.addRow(Fila);
            }
            jtable.setModel(model);
            return jtable;
        } catch (SQLException ex) {
            return jtable;
        }
    }

    public String[] particionar_ComboBox(JComboBox cb, String separacion) {
        String cb_particionado[] = null;
        try {

            cb_particionado = cb.getSelectedItem().toString().split(separacion);
            return cb_particionado;
        } catch (Exception e) {
            cb_particionado = null;
        }
        return cb_particionado;
    }

    public boolean Ejecucion(String query) {
        Connection cn = null;
        boolean exito = false;
        try {
            cn = Conexion.getConexion();
            if (cn != null) {
                Statement st = cn.createStatement();
                st.executeUpdate(query);
                exito = true;
            }
        } catch (SQLException e) {

            if (cn == null) {
                JOptionPane.showMessageDialog(null, "NO HAY CONEXION CON EL SERVIDOR");
            }
            JOptionPane.showMessageDialog(null, "Se produjo un error verifique los datos " + e.getMessage(), "Informacion", JOptionPane.INFORMATION_MESSAGE);

        } finally {

        }
        return exito;
    }

    public JTable llenarTabla(String sql, String[] valores, String[] titulos, String[] campos) {
        Connection cn2 = null;
        ResultSet rs2 = null;
        javax.swing.JTable jtable = new javax.swing.JTable();
        try {
            cn2 = Conexion.getConexion();
            CallableStatement st2 = cn2.prepareCall(sql);
            for (int i = 0; i < valores.length; i++) {
                st2.setString((i + 1), valores[i]);
            }
            rs2 = st2.executeQuery();
            jtable = this.ModeloTb_rSet(rs2, titulos, campos);
            return jtable;
        } catch (SQLException ex) {
            if (cn2 == null) {
                JOptionPane.showMessageDialog(null, "NO HAY CONEXION CON EL SERVIDOR");
            }
            //  this.JOptionPane(ex.getMessage(), sql);
            return jtable;
        } finally {
            try {
                if (cn2 != null) {
                    // cn2.close();
                }
                if (rs2 != null) {
                    rs2.close();
                }
            } catch (Exception e) {
            }
        }
    }

   

 

    public String fechaDayMonthYear() {

        Date fecha = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/YYYY");

        return formatoFecha.format(fecha).replace("/", "-");

    }

    public boolean SiRegistroExiste(String sql) {
        Connection cn2 = null;
        Statement st2 = null;
        ResultSet rs2 = null;
        try {
            cn2 = Conexion.getConexion();
            st2 = cn2.createStatement();
            rs2 = st2.executeQuery(sql);
            if (rs2.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            if (cn2 == null) {
                JOptionPane.showMessageDialog(null, "NO HAY CONEXION CON EL SERVIDOR");
            }
            return false;
        } finally {
            this.desconectar(st2, rs2, cn2);
        }
    }

    public String[][] ConsultaResultS_a_matriz(String sql, String[] campos) {
        String[][] matriz = null;
        Connection cn2 = null;
        Statement st2 = null;
        ResultSet rs2 = null;
        try {
            cn2 = Conexion.getConexion();
            st2 = cn2.createStatement();
            rs2 = st2.executeQuery(sql);
            int total_filas = 0;
            int total_col = rs2.getMetaData().getColumnCount();
            int i = 0;

            while (rs2.next()) {
                if (rs2.last()) {
                    total_filas = rs2.getRow();
                }
            }

            matriz = new String[total_filas][total_col];
            rs2.close();
            st2.close();
            st2 = cn2.createStatement();
            rs2 = st2.executeQuery(sql);

            while (rs2.next()) {
                for (int j = 0; j < campos.length; j++) {
                    matriz[i][j] = rs2.getString(campos[j]);
                }
                i++;
            }
        } catch (SQLException ex) {
            if (cn2 == null) {
                JOptionPane.showMessageDialog(null, "NO HAY CONEXION CON EL SERVIDOR");
            }
        } finally {
            this.desconectar(st2, rs2, cn2);
        }
        return matriz;
    }

    public boolean CrearCarpeta(String ruta) {
        ruta = this.GenerarRutaAbsoluta(ruta);
        File archivo = new File(ruta);
        if (archivo.mkdirs()) {
            return true;
        }
        return false;
    }

    public String GenerarRutaAbsoluta(String ruta) {
        File f = new File(".");
        try {
            if (f.getCanonicalPath().substring(f.getCanonicalPath().length() - 5, f.getCanonicalPath().length()).contains("\\dist")) {
                ruta = f.getCanonicalPath().substring(0, f.getCanonicalPath().length() - 5) + "\\" + ruta;
            } else {
                ruta = f.getCanonicalPath() + "\\" + ruta;
            }

        } catch (IOException ex) {
        }
        return ruta;
    }

    public String convertircantidad(String vnumero) {
        String cadena = "";

        try {
            String nmr = String.valueOf(vnumero);
            n2t numero;
            String res;
            int num = Integer.parseInt(nmr);
            numero = new n2t(num);
            res = numero.convertirLetras(Integer.parseInt(nmr));
            cadena = cadena + res;
        } catch (Exception err) {
        }
        return cadena;

    }

    public void JOptionPane(String texto, String titulo) {
        JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    public String[] metodoconversion(String caja) {
        String[] dts = new String[2];
        char[] ctd = new char[30];
        String numero = "";
        String decimals = "";
        int n = 0;
        ctd = caja.toCharArray();
        int i = 0;
        for (i = 0; i < ctd.length; i++) {
            if (ctd[i] != '.') {
                numero = numero + ctd[i];
            } else {
                n = i + 1;
                break;
            }
        }
        if (n > 0) {
            n = i + 1;
            System.out.print(n);
            if (n < ctd.length) {
                for (i = n; i < ctd.length; i++) {
                    if (ctd[i] != ' ') {
                        decimals = decimals + ctd[i];
                    }
                }
            }
        }
        dts[0] = numero;
        dts[1] = decimals;
        return dts;
    }

    public String leerTxt(String direccion) { //direccion del archivo

        String texto = "";

        try {
            BufferedReader bf = new BufferedReader(new FileReader(direccion));
            String temp = "";
            String bfRead;
            while ((bfRead = bf.readLine()) != null) {

                temp = temp + bfRead; //guardado el texto del archivo
            }

            texto = temp;

        } catch (Exception e) {
            System.err.println("No se encontro archivo");
        }
        return texto;

    }

    public void setImagen(JLabel LblPicture, String ruta) {
        File file = new File(ruta);
        BufferedImage read;
        try {
            read = ImageIO.read(file);
            Image scaledInstance = read.getScaledInstance(LblPicture.getWidth(), LblPicture.getHeight(), Image.SCALE_DEFAULT);
            LblPicture.setIcon(new ImageIcon(scaledInstance));
        } catch (IOException ex) {
            LblPicture.setIcon(null);
            System.out.println(ex.getMessage());
        }
    }

}
