/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author mario.dominguez
 */
public class PiernaAraña {

    // We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private static final String USER_AGENT
            = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private List<String> links = new LinkedList<String>();
    private List<String> imgs = new LinkedList<String>();
    private List<String> files = new LinkedList<String>();

    private Document htmlDocument;
    private URL url;//Tomara el valor del textField 

    /**
     * This performs all the work. It makes an HTTP request, checks the
     * response, and then gathers up all the links on the page. Perform a
     * searchForWord after the successful crawl
     *
     * @param url - The URL to visit
     * @return whether or not the crawl was successful
     */
    public boolean crawl(String url) {
        try {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            //connection.cookie("csrftoken", "c4qwLnOxWP6cCsYBrB3uHevgfcbCvz7W");
            Document htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;
            if (connection.response().statusCode() == 200) // 200 is the HTTP OK status code
            {
                System.out.println("\n**Visiting** Received web page at " + url);
            }
            if (!connection.response().contentType().contains("text/html")) {
                System.out.println("**Failure** Retrieved something other than HTML");
                return false;
            }
            Elements linksOnPage = htmlDocument.select("a[href]");
            Elements imgOnPage = htmlDocument.select("img[src]");
            Elements imgOnPage2 = htmlDocument.select("img[srcset]");

            for (Element link : linksOnPage) {
                //Identifica si es un enlace a una página o a un archivo
                //.docx, .pdf
                if (link.absUrl("href").toLowerCase().contains(".docx") || link.absUrl("href").toLowerCase().contains(".pdf")) {
                    if (!this.files.contains(link.absUrl("href"))) {
                        this.files.add(link.absUrl("href"));
                    }
                } else {
                    if (!this.links.contains(link.absUrl("href"))) {
                        this.links.add(link.absUrl("href"));
                    }
                }
            }

            for (Element link : imgOnPage) {
                if (!this.imgs.contains(link.absUrl("src"))) {
                    this.imgs.add(link.absUrl("src"));
                }
            }

            for (Element link : imgOnPage2) {
                if (!this.imgs.contains(link.absUrl("srcset"))) {
                    this.imgs.add(link.absUrl("srcset"));
                }
            }

            System.out.println("Found (" + this.links.size() + ") links");
            System.out.println("Found (" + this.imgs.size() + ") images");
            System.out.println("Found (" + this.files.size() + ") files");

            return true;
        } catch (IOException ioe) {
            // We were not successful in our HTTP request
            return false;
        }
    }

    /**
     * Performs a search on the body of on the HTML document that is retrieved.
     * This method should only be called after a successful crawl.
     *
     * @param searchWord - The word or string to look for
     * @return whether or not the word was found
     */
    public boolean searchForWord(String searchWord) {
        // Defensive coding. This method should only be used after a successful crawl.
        if (this.htmlDocument == null) {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return false;
        }
        System.out.println("Searching for the word " + searchWord + "...");
        String bodyText = this.htmlDocument.body().text();
        return bodyText.toLowerCase().contains(searchWord.toLowerCase());
    }

    /**
     * Read the images on a specific URL and save it on c:/imgtemp.
     *
     * @throws java.net.MalformedURLException
     */
    public void saveImage() throws MalformedURLException, IOException {
        creaDirectorio("imgtemp");
        System.out.println("Se procesarán " + this.imgs.size() + " imagenes");
        for (String linkImg : this.imgs) {
            System.out.println(linkImg);
            if (linkImg.trim().equals("")) {
                continue;
            }
            url = new URL(linkImg);//Inicio de la variable url 
            String[] arrRuta = linkImg.split("/");

            try {
                /* definimos la URL de la cual vamos a leer */
                url = new URL(linkImg);

                String imgName = arrRuta[arrRuta.length - 1];

                if (!imgName.contains(".svg") && !imgName.contains(".gif") && !imgName.contains(".png") && !imgName.contains(".jpg") && !imgName.contains(".jpeg")) {
                    imgName += ".png";
                }

                /* llamamos metodo para que lea de la URL y lo escriba en le fichero pasado */
                writeTo(url.openStream(), new FileOutputStream(new File("c:/imgtemp/" + imgName)));

            } catch (MalformedURLException | FileNotFoundException e) {
            } catch (IOException e) {
            }
        }
    }

    /**
     * Read the files on a specific URL and save it on c:/filetemp.
     *
     * @throws java.net.MalformedURLException
     */
    public void saveFile() throws MalformedURLException {
        creaDirectorio("filetemp");

        System.out.println("Se procesarán " + this.files.size() + " archivos");
        for (String linkFile : this.files) {
            System.out.println(linkFile);
            url = new URL(linkFile);//Inicio de la variable url 
            String[] arrRuta = linkFile.split("/");

            try {
                /* definimos la URL de la cual vamos a leer */
                url = new URL(linkFile);

                String fileName = arrRuta[arrRuta.length - 1];
                /* llamamos metodo para que lea de la URL y lo escriba en le fichero pasado */
                writeTo(url.openStream(), new FileOutputStream(new File("c:/filetemp/" + fileName)));

                //System.out.println("Archivo leido y guardado!");
            } catch (MalformedURLException | FileNotFoundException e) {
            } catch (IOException e) {
            }
        }
    }

    /**
     * Escribe in en out
     *
     * @param in
     * @param out
     * @throws IOException
     */
    public static void writeTo(InputStream in, OutputStream out) throws IOException {
        try {
            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private void creaDirectorio(final String directory) {
        File folder = new File("c:\\" + directory);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
    }

    public List<String> getLinks() {
        return this.links;
    }

    public void clearImgs() {
        this.links.clear();
    }

    public void clearFiles() {
        this.files.clear();
    }

}
