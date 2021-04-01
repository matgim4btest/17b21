package rs.edu.matgim.zadatak;

import java.sql.*;

public class DB {

    
    String connectionString = "jdbc:sqlite:src\\main\\java\\Banka.db";

    public void printFilijala() {
        try ( Connection connection = DriverManager.getConnection(connectionString);
                Statement s = connection.createStatement()) {

            ResultSet rs = s.executeQuery("SELECT * FROM Filijala");
            while (rs.next()) {
                int IdFil = rs.getInt("IdFil");
                String Naziv = rs.getString("Naziv");
                String Adresa = rs.getString("Adresa");

                System.out.println(String.format("%d\t%s\t%s", IdFil, Naziv, Adresa));
            }

        } catch (SQLException ex) {
            System.out.println("Greska prilikom povezivanja na bazu");
            System.out.println(ex);
        }
    }
    public void printPositiveRacun(){
         try ( Connection connection = DriverManager.getConnection(connectionString);
                Statement s = connection.createStatement()) {

            ResultSet rs = s.executeQuery("SELECT * FROM Racun WHERE Stanje>0");
            while (rs.next()) {
                int IdFil = rs.getInt("IdFil");
                String Naziv = rs.getString("Naziv");
                String Adresa = rs.getString("Adresa");

                System.out.println(String.format("%d\t%s\t%s", IdFil, Naziv, Adresa));
            }

        } catch (SQLException ex) {
            System.out.println("Greska prilikom povezivanja na bazu");
            System.out.println(ex);
        }
    }

    public float zadatak(int idKom,int idFil){
        int sum=0;
         try ( Connection connection = DriverManager.getConnection(connectionString);
                PreparedStatement ps = connection.prepareStatement("SELECT IdRac,Stanje,DozvMinus FROM Racunr R,Komitent K WHERE R.IdKom=K.IdKom AND R.Stanje<-R.DozvMin AND K.IdKom= ? ");
                 PreparedStatement ps2=connection.prepareStatement("INSERT INTO Uplata (Iznos,IdRac,IdFil,Osnov) Values (?,?,?,"+"'Uplata na zahtev gradjanina'"+"+)");
                 PreparedStatement ps3=connection.prepareStatement("UPDATE Racun SET Stanje=DozvMinus AND Status='A' WHERE R.Idrac=?");) {
         
             ps.setInt(1,idKom);
                ps.execute(); 
            ResultSet rs = ps.getGeneratedKeys();
            int [] negativniRac=new int[1000];
            int [] zaUplatu=new int[1000];
            int i=0;
            
            while (rs.next()) {
               negativniRac[i]=rs.getInt(1);
               zaUplatu[i]=-rs.getInt(2)-rs.getInt(3);
               sum+=zaUplatu[i];
               i++;
               
            }
                connection.setAutoCommit(false);
            for(int j=0;j<i;j++){
                ps2.setInt(1, zaUplatu[j]);
                ps2.setInt(2,negativniRac[j]);
                ps2.setInt(3,idFil);
                ps2.execute();
                ps3.setInt(1, negativniRac[j]);
                ps3.execute();
            }
            connection.commit();
            
            connection.setAutoCommit(true);
             System.out.println("Uspesna realizacija");

        } catch (SQLException ex) {
            System.out.println("Greska prilikom povezivanja na bazu");
            System.out.println(ex);
        }
        
        
        return sum;
    }
    
}
