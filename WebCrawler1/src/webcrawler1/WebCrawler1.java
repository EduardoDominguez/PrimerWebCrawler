/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler1;

import java.io.IOException;

/**
 *
 * @author mario.dominguez
 */
public class WebCrawler1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Araña spider = new Araña();
        int respuesta = spider.search("http://contraloria.leon.gob.mx/declaranet/declare.php", "MARIO EDUARDO");
    }
    
}
