import java.util.List;
import java.util.concurrent.TimeUnit;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.lang.Exception;

public class SistemAbsensiProcedural {
    public static List<String> listNama = ReadNama();
    public static List<String> listPass =  ReadPass();
    public static List<String> ReadNama(){
        List<String> result = new ArrayList<String>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader("data_pegawai.txt"));
            while(reader.readLine() != null){
                try{
                    reader.readLine();
                    result.add(reader.readLine());
                    reader.readLine();

                }catch(Exception e){
                    reader.close();
                    e.printStackTrace();
                    break;
                }
            }
            reader.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static void clrscr(){

        try {
    
            if (System.getProperty("os.name").contains("Windows"))
    
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    
            else
    
                Runtime.getRuntime().exec("clear");
    
        } catch (IOException | InterruptedException ex) {}
    
    }

    public static List<String> ReadPass(){
        List<String> result = new ArrayList<String>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader("data_pegawai.txt"));
            while(reader.readLine() != null){
                try{
                    reader.readLine();
                    reader.readLine();
                    result.add(reader.readLine());
                }catch(Exception e){
                    reader.close();
                    e.printStackTrace();
                    break;
                }
            }
            reader.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    public static void WriteFile(String id, String nama,String tanggal, String waktu, String status){
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter("riwayat.txt", true));
            out.write("#"); //Ini digunakan sebagai pembatas, agar file lebih mudah dibaca
            
            out.newLine();
            out.write("ID \t: "+id);            
            out.newLine();
            out.write("Nama \t: "+nama);                       
            out.newLine();
            out.write("Tanggal : "+tanggal);			
            out.newLine();
            out.write("Waktu \t: "+waktu);			
            out.newLine();
            out.write("Status \t: "+status);

            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String inputString(){
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        String result="";
        try {
            result = buf.readLine();
        } catch (IOException e) {
            System.out.println("Error!");
        }
        return result;
    }

    public static void mainMenu(){
        clrscr();  
        System.out.println("======== SISTEM ABSENSI PEGAWAI =========\n");

        System.out.println("==== MAIN MENU ====");
        System.out.println("1. Presensi");
        System.out.println("2. Cek Kehadiran");
        System.out.println("3. Keluar");
        System.out.print("Pilih menu (1-3) : ");

        switch(Integer.parseInt(inputString())){
            case 1 :
                Presensi();
                break;
            case 2 :
                cekKehadiran();
                break;
            case 3 :
                ExitProgram();
                break;
            default :
                System.out.println("INPUT SALAH !!!!\n");
                mainMenu();
        }
    }

    public static boolean sudahPresensi(String checknama){
        List<String> result = new ArrayList<String>();
        LocalDate today = LocalDate.now();
        try{
            BufferedReader reader = new BufferedReader(new FileReader("riwayat.txt"));
            String text;
            while((text = reader.readLine()) != null){
                int index = text.indexOf(" ");
                if(index>0 && text.substring(0, index).equals("Nama")){
                    int indexNama = text.lastIndexOf(":");
                    String nama = text.substring(indexNama+2);
                    
                    text = reader.readLine();
                    int indexTanggal = text.lastIndexOf(":");
                    String tanggal = text.substring(indexTanggal+2);
                    if(LocalDate.parse(tanggal).equals(today)){
                        result.add(nama);
                    }
                }else if(text.equals("")){
                    break;
                }
            }
            reader.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        for(int i=0; i<result.size(); ++i) {
            if(result.get(i).equals(checknama)) return true;
        }
        return false;
    }

    public static void Presensi(){
        String nama, pass;
        clrscr();  
        System.out.println("======== SISTEM ABSENSI PEGAWAI =========\n");
        
        System.out.println("==== PRESENSI ====");
        System.out.print("Masukkan nama \t\t : ");
        nama = inputString();
        System.out.print("Masukkan password \t : ");
        pass = inputString();

        for(int i = 0; i<listNama.size(); ++i){
            if(nama.equals(listNama.get(i)) && pass.equals(listPass.get(i))){
                System.out.print("\033[H\033[2J");  
                System.out.flush();
                if(!sudahPresensi(nama)){
                    System.out.println(listNama.get(i)+" berhasil melakukan presensi");
                    String status = LocalTime.now().isBefore(LocalTime.parse("08:00:00"))? "masuk" : "terlambat"; 
                    WriteFile(String.valueOf(i+1), nama, LocalDate.now().toString(), LocalTime.now().toString(), status);
                    
                    System.out.print("\nTekan [Enter] untuk menuju main menu");
                    inputString();
                    mainMenu();
                }else{
                    System.out.println("Anda sudah pernah melaukan presensi hari ini..");
                    System.out.print("\nTekan [Enter] untuk menuju main menu");
                    inputString();
                    mainMenu();
                }
            }
        }
        System.out.println("Nama dan/atau Password salah !!");
        System.out.print("Ulangi [y/n]? : ");
        String ulang =  inputString();
        if(ulang.equals("y")) Presensi();
        else mainMenu();
    }

    public static void cekKehadiran(){
        clrscr();  
        System.out.println("======== SISTEM ABSENSI PEGAWAI =========\n");
        
        System.out.println("==== CEK KEHADIRAN ====");
        LocalDate today = LocalDate.now();
        try{
            BufferedReader reader = new BufferedReader(new FileReader("riwayat.txt"));
            String text;
            while((text = reader.readLine()) != null){
                int index = text.indexOf(" ");
                if(index>0 && text.substring(0, index).equals("Nama")){
                    // --- Ambil Nama ---
                    int indexNama = text.lastIndexOf(":");
                    String nama = text.substring(indexNama+2);
                    
                    // --- Ambil tanggal ---
                    text = reader.readLine();
                    int indexTanggal = text.lastIndexOf(":");
                    String tanggal = text.substring(indexTanggal+2);
                    if(LocalDate.parse(tanggal).equals(today)){
                        // --- Ambil waktu ---
                        text = reader.readLine();
                        int indexWaktu = text.lastIndexOf(":");
                        String waktu = text.substring(indexWaktu+2);

                        // --- Ambil staus ---
                        text = reader.readLine();
                        int indexStatus = text.lastIndexOf(":");
                        String status = text.substring(indexStatus+2);

                        System.out.println("Nama \t: "+nama);
                        System.out.println("Waktu \t: "+waktu);
                        System.out.println("Status \t: "+tanggal);
                        System.out.println("====================");
                    }
                }else if(text.equals("")){
                    break;
                }
            }
            reader.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.print("\nTekan [Enter] Untuk Kembali");
        inputString();
        mainMenu();
    }

    public static void ExitProgram(){
        clrscr();  

        try{
            System.out.println("==== TERIMA KASIH ====");
            TimeUnit.SECONDS.sleep(1);
            System.exit(0);
        }catch(InterruptedException e){
            System.err.format("IOException: %s%n", e);
        }
    }
    public static void main(String[] args) {
        mainMenu();
    }
}
