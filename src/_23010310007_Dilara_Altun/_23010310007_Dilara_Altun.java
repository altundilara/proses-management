package _23010310007_Dilara_Altun;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.File;

public class _23010310007_Dilara_Altun {

	public static void main(String[] args) throws Exception {
		LinkedList<Proses> gelenProsesler=new LinkedList<>();       
		try{
            Scanner scanner=new Scanner(new File("prosesler.txt"));        
            while(scanner.hasNextLine()){
                String satir=scanner.nextLine().trim();
                if(satir.isEmpty()) continue;
                
                String[] parcalar=satir.split("\\s+");
                String isim=parcalar[0];
                int gelisZamani=Integer.parseInt(parcalar[1]);
                int burstZamani=Integer.parseInt(parcalar[2]);
                
                gelenProsesler.add(new Proses(isim, gelisZamani, burstZamani));}            
            scanner.close();
            System.out.println("prosesler.txt dosyası okundu.");
            
        }catch (Exception e){
            System.out.println("Dosya okuma veya format hatası oluştu.");
            e.printStackTrace();
            return;}

        System.out.println("SJF (non-preemptive) Simülasyonu:");
        SjfZamanlayici scheduler=new SjfZamanlayici(gelenProsesler);
        scheduler.simulasyonuBaslat();}
}
class Proses{
    private String isim;
    private int gelisZamani;
    private int burstZamani;
    private int calisanSure;
    private int beklemeZamani;

public Proses(String isim, int gelisZamani, int burstZamani) {
        this.isim = isim;
        this.gelisZamani=gelisZamani;
        this.burstZamani=burstZamani;
        this.calisanSure=0;
        this.beklemeZamani=0;}

   public String getIsim(){ 
	   return isim;}
    public int getGelisZamani(){
    	return gelisZamani;}
    public int getBurstZamani(){
    	return burstZamani;}
    public int getCalisanSure(){
    	return calisanSure;}
    public int getBeklemeZamani(){
    	return beklemeZamani;}

    public void setCalisanSure(int calisanSure) {
    	this.calisanSure=calisanSure;}
    public void setBeklemeZamani(int beklemeZamani){this.beklemeZamani=beklemeZamani;}
}
class SjfZamanlayici{
    private LinkedList<Proses> gelenProsesler;
    private LinkedList<Proses> beklemeKuyrugu;
    private LinkedList<Proses> bitenProsesler;
    private Proses aktifProses;
    private int zaman;

   public SjfZamanlayici(LinkedList<Proses> gelenProsesler){
        this.gelenProsesler=gelenProsesler;
        this.beklemeKuyrugu=new LinkedList<>();
        this.bitenProsesler=new LinkedList<>();
        this.aktifProses=null;
        this.zaman=0;}

   private void kuyrugaEkle(Proses yeni){
       int index=0;
       for (Proses p:beklemeKuyrugu){
           if(yeni.getBurstZamani()< p.getBurstZamani()){
               beklemeKuyrugu.add(index, yeni);
               return;}
           else if(yeni.getBurstZamani()== p.getBurstZamani() && yeni.getGelisZamani()== p.getGelisZamani()){
               if (yeni.getIsim().compareTo(p.getIsim())<0){
                   beklemeKuyrugu.add(index, yeni);
                   return;}
           }           
           index++;
       }
       beklemeKuyrugu.addLast(yeni);}

    private void istatistikleriYazdir(){
    	bitenProsesler.sort((p1, p2) -> p1.getIsim().compareTo(p2.getIsim()));

        System.out.print("Bekleme Zamanları: ");
        int toplam=0;
        for (int i=0; i < bitenProsesler.size(); i++){
            Proses p=bitenProsesler.get(i);
            toplam +=p.getBeklemeZamani();
            if(i ==bitenProsesler.size()- 1){
                System.out.print(p.getIsim()+ " "+ p.getBeklemeZamani()+ " saniye");}
            else{
                System.out.print(p.getIsim()+ " "+ p.getBeklemeZamani()+ " saniye, ");}
        }
        System.out.println();
        double ortalama=(double) toplam / bitenProsesler.size();
        System.out.printf("Ortalama Bekleme Süresi: %.2f Saniye%n", ortalama);}

  public void simulasyonuBaslat() throws InterruptedException{
        while (true){
            Iterator<Proses> iterator=gelenProsesler.iterator();
            while(iterator.hasNext()){
                Proses p=iterator.next();
                if(p.getGelisZamani()==zaman){
                	kuyrugaEkle(p);
                    iterator.remove();
                    System.out.println(p.getIsim()+ " prosesi " + zaman + ". Saniyede kuyruğa girdi.");}
}
     
            if(aktifProses== null && !beklemeKuyrugu.isEmpty()){
                aktifProses=beklemeKuyrugu.removeFirst();
                aktifProses.setBeklemeZamani(zaman- aktifProses.getGelisZamani());
                System.out.println(aktifProses.getIsim() + " prosesi "+ zaman+ ". Saniyede çalışmaya başladı.");}

            if(aktifProses !=null){
                aktifProses.setCalisanSure(aktifProses.getCalisanSure()+1);
                System.out.println(aktifProses.getIsim()+ " prosesi toplamda "+ aktifProses.getCalisanSure()+ " saniye çalıştı.");
                if(aktifProses.getCalisanSure()== aktifProses.getBurstZamani()){                   
                	bitenProsesler.add(aktifProses);
                    aktifProses=null;}
            }
            if(gelenProsesler.isEmpty() && beklemeKuyrugu.isEmpty() && aktifProses==null){
               break;}

            Thread.sleep(1000);
            zaman++;}
        istatistikleriYazdir();
  }
}