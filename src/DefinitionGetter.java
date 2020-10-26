import com.alibaba.fastjson.JSON;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

@WebServlet("/getDefinition/*")
public class DefinitionGetter extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
         * Get definition from http://lexico.com and create a JSON response for the definitions
         *
         * Parameters:
         *
         */

        String contextPath = req.getPathInfo();
        // The last part of the URL? Probably... I don't how to say it
        // Here's an example: if the url is http://localhost:8080/getDefinition/test
        // Then contextPath should be "/test"

        String requestedWord = contextPath.substring(1); // The word the user requested
        Connection connection = Jsoup.connect("https://www.lexico.com/en/definition/" + requestedWord);
        // The connection object we use to obtain both the document and the status code
        if (connection.response().statusCode() == 404) {
            try {
                resp.getWriter().write(JSON.toJSONString(new ResponseContent(requestedWord,404)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Document doc = connection.get();

            int senseNum = doc.select("span.hw > sup").size();
            if (senseNum == 0) {
                senseNum = 1;
            }

            ArrayList<String>[] allPos = new ArrayList[senseNum];

            ArrayList<Definition>[] defsOfSenses = new ArrayList[senseNum];
            Elements entryHeadOfSenses = doc.select("div.entryHead"); // The header of each entry

            for (int curSense = 0; curSense < senseNum; curSense++) {
                Element curSenseEntryHead = entryHeadOfSenses.get(curSense);
                Elements defSectionOfEachPosOfAllSenses = curSenseEntryHead.siblingElements().select("section.gramb");
                // We now get all <section> elements that contain defs that belong to all senses


                // To get rid of the <section> elements that belong to other senses,
                // we need to do some checks and removals first
                Elements entryHeadOfOtherSenses = new Elements(entryHeadOfSenses);
                entryHeadOfOtherSenses.remove(curSense);




                Elements defSectionOfEachPosOfCurSense = new Elements();
                for (Element defSectionOfPos : defSectionOfEachPosOfCurSense) {
                    String curPos = defSectionOfPos.selectFirst("h3.pos").selectFirst("span.pos").text();
                    if (allPos[curSense] == null) allPos[curSense] = new ArrayList<>();
                    allPos[curSense].add(curPos);
                    ArrayList<String> defsOfCurPos = new ArrayList<>();

                    Elements defItemsOfCurPos = defSectionOfPos.selectFirst("ul.semb").children();
                    for (Element defItemOfCurPos : defItemsOfCurPos) {
                        String defText = defItemOfCurPos.selectFirst("div.trg").selectFirst("span.ind").text();
                        defsOfCurPos.add(defText);
                    }

                    Definition defObjOfCurPos = new Definition(curPos, defsOfCurPos);
                    if (defsOfSenses[curSense] == null) defsOfSenses[curSense] = new ArrayList<>();
                    defsOfSenses[curSense].add(defObjOfCurPos);
                }

            }


            ResponseContent responseContent = new ResponseContent(requestedWord,
                    new ArrayList<ArrayList<String>>(Arrays.asList(allPos)),
                    new ArrayList<ArrayList<Definition>>(Arrays.asList(defsOfSenses)));
            String json = JSON.toJSONString(responseContent);
            resp.getWriter().write(json);
        }
    }
}
