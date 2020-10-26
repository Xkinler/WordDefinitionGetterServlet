import com.alibaba.fastjson.JSON;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
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
import java.util.ArrayList;
import java.util.Arrays;

@WebServlet("/getDefinition/*")
public class DefinitionGetter extends HttpServlet {

    private static boolean checkClosestPreviousEntryHeadElement(Element element, Element entryHead) {

        /*
        * This method is used to check if the closest previous entry head element
        * of the element specified by the element argument,
        * is entryHead, specified by the argument entryHead
        *
        * if so, it means that the specified element is one of those that belongs to the sense
        * whose entry head is entryHead
        */

        Element cur = element.previousElementSibling();
        // Find the closest entry head element
        while (cur != null && (!cur.tagName().equals("div") || !cur.hasClass("entryHead"))) {
            cur = cur.previousElementSibling();
        }

        Element closestEntryHead = cur;
        return closestEntryHead != null && closestEntryHead.id().equals(entryHead.id());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
         * Get definition from http://lexico.com and create a JSON response for the definitions
         */

        String contextPath = req.getPathInfo();
        // The last part of the URL? Probably... I don't how to say it
        // Here's an example: if the url is http://localhost:8080/getDefinition/test
        // Then contextPath should be "/test"

        String requestedWord = contextPath.substring(1); // The word the user requested
        try {
            Connection.Response response;
            response = Jsoup.connect("https://www.lexico.com/en/definition/" + requestedWord).execute();
            // Just too long to be put in one line....

            // The response we get from requesting the URL for word definitions.
            Document doc = response.parse();

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
                // (as they are all sibling elements to each other)


                Elements defSectionOfEachPosOfCurSense = new Elements();
                for (Element section: defSectionOfEachPosOfAllSenses) {
                    if (checkClosestPreviousEntryHeadElement(section, curSenseEntryHead)) {
                        defSectionOfEachPosOfCurSense.add(section);
                    }
                }




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
            resp.setContentType("application/json");
            resp.getWriter().write(json);
        } catch (HttpStatusException e) {
            if (e.getStatusCode() == 404) {
                try {
                    resp.getWriter().write(JSON.toJSONString(new ResponseContent(requestedWord,404)));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
}
