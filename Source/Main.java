package com.cuhlifa.Parser;

import net.minecraft.util.org.apache.commons.io.FilenameUtils;
import org.jsoup.*;
import org.jsoup.nodes.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Connor Peticca on 10/9/2014.
 */
public class Main {

    public static void main(String[] args) {

        while (true) {

            Document doc = null;
            try {


                doc = Jsoup.connect("https://twitter.com/SJDBScience").get();
                for (Element e : doc.getElementsByClass("ProfileTweet-text")) {

                    for (Element e1 : e.getElementsByClass("twitter-timeline-link")) {

                        for(Element e2 : e.getElementsByClass("twitter-hashtag")){

                            if(e2.text().equalsIgnoreCase("#sjdbSPH3U")){

                                prepareForDownload(e1.attr("href"));

                            }

                        }



                    }


                }

                Thread.sleep(100000);

            } catch (Exception e) {
            }

        }

    }


    public static void downloadFile(String fileURL, String saveDir, String fileName)
            throws IOException {

        URL link = new URL(fileURL); //The file that you want to download

        if(fileName.contains("1.") || fileName.contains("2.")){

            saveDir = "/root/repo/Unit 1";

        }

        if(fileName.contains("3.")){

            saveDir = "/root/repo/Unit 2";

        }

        //Code to download
        URLConnection con = link.openConnection();
        InputStream in = new BufferedInputStream(con.getInputStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[8196];
        int n = 0;
        while (-1!=(n=in.read(buf)))
        {
            out.write(buf, 0, n);
        }
        out.close();
        in.close();
        byte[] response = out.toByteArray();

        FileOutputStream fos = new FileOutputStream(new File(saveDir,fileName));
        fos.write(response);
        fos.close();

        System.out.println("Saved file: " + fileName + " to directory: " + saveDir + " from url: " + link);

    }

    public static void prepareForDownload(String url2) {

        try {

            String url1 = retriveDownloadLink(url2);
            url1 = retriveDownloadLink(url1).replace("redir", "download");
            url1 = retriveDownloadLink(url1);
            URL link = new URL(url1); //The file that you want to download

            String fileName = FilenameUtils.getName(url1).replace("%20"," ").replace("?download&psid=1","");

            if(!fileName.contains("=") && !fileName.contains("/")){

                downloadFile(url1, "/root/repo/",fileName);

            }

        } catch (Exception e) {
        }

    }

    public static String retriveDownloadLink(String url) {

        try {
            HttpURLConnection con = (HttpURLConnection) new URL(
                    url).openConnection();
            con.connect();
            con.setInstanceFollowRedirects(false);
            int responseCode = con.getResponseCode();
            if ((responseCode / 100) == 3) {
                String newLocationHeader = con.getHeaderField("Location");
                return newLocationHeader;
            }
        } catch (Exception e) {
        }
        return url;
    }

}
