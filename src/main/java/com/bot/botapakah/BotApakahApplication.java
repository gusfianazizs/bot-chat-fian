package com.bot.botapakah;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse.MessageContentResponseBuilder;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.json.*;

import java.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Random;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
@LineMessageHandler
public class BotApakahApplication extends SpringBootServletInitializer {

    String pesan_dikirim="";
    String pesan_dua="";
    String gambar="";
    String pesan2="";
    String id="";
    String info="\n\nJika ada yang mau ditanyakan lagi bisa tanya disini ka atau datang dan kunjungi social media kami\n\nKawasan Pendidikan Telkom Jl. DI. Panjaitan No. 128 Purwokerto 53147, Jawa Tengah.\nFan Page: It Telkom Purwokerto\nInstagram: @join_ittp\nLine ID: @join_ittp\nWA/SMS/Telp: 081228319222\nHunting: (0281) – 641629, Fax : (0281) 641630";
    String ya="";

    @Autowired
    private LineMessagingClient lineMessagingClient;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BotApakahApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(BotApakahApplication.class, args);
    }

    // public static String charRemoveAt(String str, int p) {  
    //     return str.substring(0, p) + str.substring(p + 1);  
    //  }

    @EventMapping
    public void handleTextEvent(MessageEvent<TextMessageContent> messageEvent){
        String pesan = messageEvent.getMessage().getText().toLowerCase();
        id = messageEvent.getSource().getUserId();
        String replyToken = messageEvent.getReplyToken();
        char cek = pesan.charAt(pesan.length()-1);
        Boolean lanjut=false;
        
        System.out.println("Isi Pesan :"+pesan);
        System.out.println("Panjang Pesan :"+pesan.length());
        System.out.println("Harusnya si tanda tanya :"+cek);

        pesan_dikirim="";

        
        // System.out.println("Panjang data : "+panjang);
        // System.out.println("Isi data : "+pesanSplit);

        switch(pesan) {
            case "/rules":
                String rules="Berikut aturan untuk menggunakan Chat Bot Custumer Service IT Telkom Purwokerto\n1. Gunakanlah bahasa yang baku.\n2. Perhatikan tulisan yang anda ketik.";
                balasChatDenganRandomJawaban(replyToken, rules, ""+gambar);
            break;
            case "terima kasih":
                String terimakasih="Terima Kasih sudah menggunakan aplikasi ini.";
                balasChatDenganRandomJawaban(replyToken, terimakasih, ""+gambar);
            break;
            case "ya":
                if(pesan2=="true"){
                    balasChatDenganRandomJawaban(replyToken, pesan_dua+info, ""+gambar);
                    pesan2="";
                    pesan_dua="";
                    gambar="";
                    ya="";
                }else{
                    String error="Mohon untuk memperhatikan bahasa yang anda gunakan.\nUntuk informasi lebih lanjut, anda bisa membaca aturan yang ditentukan.\nSilahkan ketik '/rules', Terima Kasih.";
                    balasChatDenganRandomJawaban(replyToken, error, ""+gambar);
                }
            break;
            default:
                if(cek=='?'){
                    compare(pesan);
                    lanjut=true;
                }else{
                    String tandatanya="mohon untuk memberi tanda tanya '?' dan pastikan bahwa tidak ada huruf / character dibelakang tanda tanya.";
                    balasChatDenganRandomJawaban(replyToken, tandatanya, ""+gambar);
                }

                if(lanjut==true && ya=="yes"){
                    balasChatDenganRandomJawaban(replyToken, pesan_dikirim, ""+gambar);
                }else{
                    balasChatDenganRandomJawaban(replyToken, pesan_dikirim+info, "null");
                }
            break;
        }
    }

    private void compare(String isi_kiriman){
        String cleartext = isi_kiriman.substring(0, isi_kiriman.length()-1);
        System.out.println("Hasil clear text : "+cleartext);
        String[] pesanSplit = cleartext.split(" ");
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("./data.csv"))) {
            String line;
            if((line = br.readLine()) == null){
                
            }else{
                while ((line = br.readLine()) != null) {
                    // String[] values = line.split(";");
                    records.add(line.split(";"));
                }
                String[][] array = new String[records.size()][0];
                records.toArray(array);

                for(int i=0;i<array.length;i++){
                    int batas_minimal=0;
                    String[] keyword = array[i][1].split(" ");
                    System.out.println("Isi Array : "+array[i]);
                    
                    for(int j=0;j<keyword.length;j++){
                        System.out.println("Keyword Array ke " + i + " : "+keyword[j]);

                        for(int k=0;k<pesanSplit.length;k++){
                            if(keyword[j].equals(pesanSplit[k])){
                                batas_minimal=batas_minimal+1;
                                System.out.println("Isi pesan : " +pesanSplit[k]);
                            }
                        }
                        System.out.println("Jumlah Batas : " +batas_minimal);
                    }
                    if(batas_minimal>=Integer.parseInt(array[i][2])){
                        if(array[i][4].equals("Yes")){
                            String hasil = array[i][3].replace("<>","\n");
                            int idpesan = Integer.parseInt(array[i][5]);
                            System.out.print("Harusnya id pesan 2:"+idpesan);
                            String hasil2 = array[idpesan-1][3].replace("<>","\n");
                            String img = array[idpesan-1][6];
                            // String img="";
                            System.out.print("Harusnya hasil pesan 2:"+hasil2);

                            pesan(hasil);
                            pesangambar(img);
                            System.out.print("Harusnya link gambar :"+img);
                            pesankedua(hasil2);
                            pesan2="true";
                        }else{
                            String hasil = array[i][3].replace("<>","\n"); // Replace 'h' with 's' 
                            String img = array[i][6];
                            System.out.print("Harusnya link gambar :"+img); 
                            pesangambar(img);
                            pesan(hasil);
                            ya="yes";
                        }
                        break;
                    }else{
                        String error="Mohon untuk memperhatikan bahasa yang anda gunakan.\nUntuk informasi lebih lanjut, anda bisa membaca aturan yang ditentukan.\nSilahkan ketik '/rules', Terima Kasih.";
                        pesan(error);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pesan(String pesan){
        this.pesan_dikirim=pesan;
    }

    private void pesangambar(String gambar){
        this.gambar=gambar;
    }

    private void pesankedua(String pesan){
        this.pesan_dua=pesan;
    }

    private void balasChatDenganRandomJawaban(String replyToken, String jawaban, String gambar){
        if(gambar.equals("null")){
            TextMessage jawabanDalamBentukTextMessage = new TextMessage(jawaban);
            try {
                lineMessagingClient
                    .replyMessage(new ReplyMessage(replyToken, jawabanDalamBentukTextMessage))
                    .get();
            } catch (InterruptedException|ExecutionException e) {
                System.out.println("Ada error saat ingin membalas chat biasa");
            }
        }else{
            TextMessage jawabanDalamBentukTextMessage = new TextMessage(jawaban);
            ImageMessage jawabanGambar = new ImageMessage(gambar,gambar);
            System.out.println("Harusnya gambar :"+gambar);
            List<Message> multipesan=new ArrayList<>();
            Set<String> userid = new HashSet<>();
    
            userid.add(id);
            multipesan.add(jawabanDalamBentukTextMessage);
            multipesan.add(jawabanGambar);
    
            Multicast multi = new Multicast(userid,multipesan);
            try {
                lineMessagingClient
                        .multicast(multi)
                        .get();
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Ada error saat ingin membalas chat gambar");
            }
        }
    }

}
