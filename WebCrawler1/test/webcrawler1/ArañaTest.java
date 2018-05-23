/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler1;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mario.dominguez
 */
public class ArañaTest {
    
    public ArañaTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of search method, of class Araña.
     */
    @Test
    public void testSearch() throws Exception {
        System.out.println("search");
        String url = "https://www.instagram.com/marthalizzmunguia/";
        String searchWord = "popo";
        Araña instance = new Araña();
        int respuesta = instance.search(url, searchWord);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
        assertEquals(10, respuesta);
    }
    
}
