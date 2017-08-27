package com.matthewmanopoli;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static DB db = new DB("/home/mmanopoli/IntelliJProjects/WebCrawlers/ProgramCreekWebCrawler/SQLite3DB.db");

    public static void main(String[] args) throws SQLException, IOException {
        db.runSql2("delete from Record;");
        URL url = new URL("https://www.python.org/about/gettingstarted/");
        processPage(url,"Monty");
    }

    public static void processPage(URL url,String textToFind) throws SQLException, IOException{

        //check if the given URL is already in database
        String sql = "select * from Record where URL = '"+url.toString()+"'";
        ResultSet rs = db.runSql(sql);
        if(rs.next()){
            // if you has a .next() then it has an element
            // So - do nothing
        }else{
            //store the URL to database to avoid parsing again
            sql = "INSERT INTO  `Record` " + "(`URL`) VALUES " + "(?);";
            PreparedStatement stmt = db.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, url.toString());
            stmt.execute();

            //get useful information
            Document doc = Jsoup.connect(url.toString()).ignoreContentType(true).ignoreHttpErrors(true).get();

            if(doc.text().contains(textToFind)){
                System.out.println(url);
            }

            //get all links and recursively call the processPage method
            Elements questions = doc.select("a[href]");
            for(Element link: questions){
                if(link.attr("href").contains(url.getHost().replaceFirst("^(www\\.|status\\.)","")))
                    processPage(new URL(link.attr("abs:href")),textToFind);
            }
        }
    }
}
