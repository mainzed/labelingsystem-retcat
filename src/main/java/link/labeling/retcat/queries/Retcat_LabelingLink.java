package link.labeling.retcat.queries;

import link.labeling.retcat.items.RetcatItems;
import link.labeling.retcat.classes.SuggestionItem;
import link.labeling.retcat.classes.RetcatItem;
import link.labeling.retcat.exceptions.ResourceNotAvailableException;
import link.labeling.retcat.exceptions.RetcatException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import link.labeling.retcat.utils.RetcatUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Retcat_LabelingLink {

    public static JSONArray query(String searchword) throws IOException, ResourceNotAvailableException, ParseException {
        JSONArray out = new JSONArray();
        String url = "http://labeling.link/api/v1/sparql";
        String sparql = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> PREFIX ls: <http://labeling.link/docs/ls/core#> PREFIX dc: <http://purl.org/dc/elements/1.1/> PREFIX dct: <http://purl.org/dc/terms/> PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
                + "SELECT ?Subject ?prefLabel ?scopeNote ?BroaderPreferredTerm ?BroaderPreferred ?NarrowerPreferredTerm ?NarrowerPreferred ?schemeTitle ?firstName ?lastName ?orcid WHERE { "
                + "?Subject skos:inScheme ?scheme . "
                + "?scheme dc:title ?schemeTitle . "
                + "OPTIONAL { ?Subject dct:creator ?creator . ?creator foaf:firstName ?firstName . ?creator foaf:lastName ?lastName . ?creator dct:publisher ?orcid . }"
                + "?Subject ls:thumbnail ?prefLabel . "
                + "OPTIONAL {?Subject skos:prefLabel ?pl . } "
                + "?scheme ls:hasReleaseType ls:Public . "
                + "OPTIONAL { ?Subject skos:scopeNote ?scopeNote . } "
                + "OPTIONAL {?Subject skos:broader ?BroaderPreferred . ?BroaderPreferred ls:thumbnail ?BroaderPreferredTerm.} "
                + "OPTIONAL {?Subject skos:narrower ?NarrowerPreferred . ?NarrowerPreferred ls:thumbnail ?NarrowerPreferredTerm .} "
                + "FILTER(regex(?pl, '" + searchword + "', 'i') || regex(?scopeNote, '" + searchword + "', 'i') || regex(?prefLabel, '" + searchword + "', 'i')) "
                + "}";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/sparql-results+json");
        String urlParameters = "query=" + sparql;
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
        writer.write(urlParameters);
        writer.close();
        wr.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF8"));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        // init output
        JSONArray outArray = new JSONArray();
        // parse SPARQL results json
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(response.toString());
        JSONObject resultsObject = (JSONObject) jsonObject.get("results");
        JSONArray bindingsArray = (JSONArray) resultsObject.get("bindings");
        // create unique list of ids
        HashSet<String> uris = new HashSet<String>();
        for (Object element : bindingsArray) {
            JSONObject tmpElement = (JSONObject) element;
            JSONObject subject = (JSONObject) tmpElement.get("Subject");
            String subjectValue = (String) subject.get("value");
            uris.add(subjectValue);
        }
        // create list of autosuggest objects
        Map<String, SuggestionItem> autosuggests = new HashMap<String, SuggestionItem>();
        for (String element : uris) {
            autosuggests.put(element, new SuggestionItem(element));
        }
        // fill objects
        for (Object element : bindingsArray) {
            JSONObject tmpElement = (JSONObject) element;
            // get Subject
            JSONObject subject = (JSONObject) tmpElement.get("Subject");
            String subjectValue = (String) subject.get("value");
            // for every subject value get object from list and write values in it
            SuggestionItem tmpAutosuggest = autosuggests.get(subjectValue);
            // get Label
            JSONObject labelObject = (JSONObject) tmpElement.get("prefLabel");
            if (labelObject != null) {
                String labelValue = (String) labelObject.get("value");
                String labelLang = (String) labelObject.get("xml:lang");
                tmpAutosuggest.setLabel(labelValue);
                tmpAutosuggest.setLanguage(labelLang);
            }
            // get Scheme
            JSONObject schemeObject = (JSONObject) tmpElement.get("schemeTitle");
            String schemeValue = (String) schemeObject.get("value");
            String schemeLang = (String) schemeObject.get("xml:lang");
            tmpAutosuggest.setSchemeTitle(schemeValue);
            // get ORCID
            JSONObject oridObject = (JSONObject) tmpElement.get("orcid");
            if (oridObject != null) {
                String oridValue = (String) oridObject.get("value");
                tmpAutosuggest.setOrcid(oridValue);
            }
            // get name
            JSONObject firstNameObject = (JSONObject) tmpElement.get("firstName");
            JSONObject lastNameObject = (JSONObject) tmpElement.get("lastName");
            if (firstNameObject != null && lastNameObject != null) {
                String firstNameValue = (String) firstNameObject.get("value");
                String lastNameValue = (String) lastNameObject.get("value");
                tmpAutosuggest.setCreator(firstNameValue + " " + lastNameValue);
            }
            // get scopeNote
            JSONObject scopeNoteObject = (JSONObject) tmpElement.get("scopeNote");
            if (scopeNoteObject != null) {
                String scopeNoteValue = (String) scopeNoteObject.get("value");
                String scopeNoteLang = (String) scopeNoteObject.get("xml:lang");
                tmpAutosuggest.setDescription(scopeNoteValue);
            }
            // get broader
            String broaderVL = "";
            String broaderURI = "";
            JSONObject broaderObject = (JSONObject) tmpElement.get("BroaderPreferredTerm");
            if (broaderObject != null) {
                String broaderValue = (String) broaderObject.get("value");
                String broaderLang = (String) broaderObject.get("xml:lang");
                broaderVL = broaderValue.replace("<", "").replace(">", "");
            }
            JSONObject broaderURIObject = (JSONObject) tmpElement.get("BroaderPreferred");
            if (broaderURIObject != null) {
                broaderURI = (String) broaderURIObject.get("value");
            }
            if (!broaderURI.equals("")) {
                HashMap<String, String> hstmpBroader = new HashMap<String, String>();
                hstmpBroader.put(broaderURI, broaderVL);
                tmpAutosuggest.setBroaderTerm(hstmpBroader);
            }
            // get narrower
            String narrowerVL = "";
            String narrowerURI = "";
            JSONObject narrowerObject = (JSONObject) tmpElement.get("NarrowerPreferredTerm");
            if (narrowerObject != null) {
                String narrowerValue = (String) narrowerObject.get("value");
                String narrowerLang = (String) narrowerObject.get("xml:lang");
                narrowerVL = narrowerValue.replace("<", "").replace(">", "");
            }
            JSONObject narrowerURIObject = (JSONObject) tmpElement.get("NarrowerPreferred");
            if (narrowerURIObject != null) {
                narrowerURI = (String) narrowerURIObject.get("value");
            }
            if (!narrowerURI.equals("")) {
                HashMap<String, String> hstmpNarrower = new HashMap<String, String>();
                hstmpNarrower.put(narrowerURI, narrowerVL);
                tmpAutosuggest.setNarrowerTerm(hstmpNarrower);
            }
            // get retcat info
            String type = "ls";
            String quality = "";
            String group = "";
            for (RetcatItem item : RetcatItems.getReferenceThesaurusCatalogue()) {
                if (item.getType().equals(type)) {
                    quality = item.getQuality();
                    group = item.getGroup();
                }
            }
            tmpAutosuggest.setType(type);
            tmpAutosuggest.setQuality(quality);
            tmpAutosuggest.setGroup(group);
        }
        // fillOutputJSONforQuery
        out = RetcatUtils.fillOutputJSONforQuery(autosuggests, searchword);
        return out;
    }

    public static JSONObject info(String url) throws IOException, ResourceNotAvailableException, ParseException, RetcatException {
        try {
            String sparqlendpoint = "http://labeling.link/api/v1/sparql";
            String sparql = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> PREFIX ls: <http://labeling.link/docs/ls/core#> PREFIX dc: <http://purl.org/dc/elements/1.1/> PREFIX dct: <http://purl.org/dc/terms/> PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
                    + "SELECT * { "
                    + "<" + url + "> ls:thumbnail ?prefLabel. "
                    + "<" + url + "> skos:inScheme ?scheme . "
                    + "?scheme ls:hasReleaseType ?releaseType . "
                    + "?scheme dc:title ?schemeTitle . "
                    + "OPTIONAL { ?Subject dct:creator ?creator . ?creator foaf:firstName ?firstName . ?creator foaf:lastName ?lastName . ?creator dct:publisher ?orcid . }"
                    + "OPTIONAL { <" + url + "> skos:scopeNote ?scopeNote . } "
                    + "OPTIONAL {<" + url + "> skos:broader ?BroaderPreferred . ?BroaderPreferred ls:thumbnail ?BroaderPreferredTerm. } "
                    + "OPTIONAL {<" + url + "> skos:narrower ?NarrowerPreferred . ?NarrowerPreferred ls:thumbnail ?NarrowerPreferredTerm . } "
                    + " }";
            URL obj = new URL(sparqlendpoint);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept", "application/sparql-results+json");
            String urlParameters = "query=" + sparql;
            byte[] bytes = urlParameters.getBytes(StandardCharsets.UTF_8);
            urlParameters = new String(bytes, StandardCharsets.ISO_8859_1);
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF8"));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // init output
            JSONObject jsonOut = new JSONObject();
            // parse SPARQL results json
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(response.toString());
            JSONObject resultsObject = (JSONObject) jsonObject.get("results");
            JSONArray bindingsArray = (JSONArray) resultsObject.get("bindings");
            // create unique list of ids
            if (!bindingsArray.isEmpty()) {
                for (Object element : bindingsArray) {
                    JSONObject tmpElement = (JSONObject) element;
                    JSONObject prefLabel = (JSONObject) tmpElement.get("prefLabel");
                    String labelValue = "";
                    String labelLang = "";
                    String stValue = "";
                    if (prefLabel != null) {
                        labelValue = (String) prefLabel.get("value");
                        labelLang = (String) prefLabel.get("xml:lang");
                        jsonOut.put("label", labelValue);
                        jsonOut.put("lang", labelLang);
                    } else {
                        jsonOut.put("label", "");
                    }
                    JSONObject releaseType = (JSONObject) tmpElement.get("releaseType");
                    stValue = (String) releaseType.get("value");
                    jsonOut.put("type", "ls");
                    jsonOut.put("releaseType", stValue.replace("http://labeling.link/docs/ls/core#", ""));
                }
                for (Object element : bindingsArray) {
                    JSONObject tmpElement = (JSONObject) element;
                    JSONObject scopeNote = (JSONObject) tmpElement.get("scopeNote");
                    String descValue = "";
                    if (scopeNote != null) {
                        descValue = (String) scopeNote.get("value");
                    }
                    jsonOut.put("description", descValue);
                }
                for (Object element : bindingsArray) {
                    JSONObject tmpElement = (JSONObject) element;
                    JSONObject scopeNote = (JSONObject) tmpElement.get("schemeTitle");
                    String descValue = (String) scopeNote.get("value");
                    jsonOut.put("scheme", descValue);
                }
                for (Object element : bindingsArray) {
                    JSONObject tmpElement = (JSONObject) element;
                    JSONObject orcid = (JSONObject) tmpElement.get("orcid");
                    String ordidValue = (String) orcid.get("value");
                    jsonOut.put("orcid", ordidValue);
                }
                String firstNameValue = "";
                String lastNameValue = "";
                for (Object element : bindingsArray) {
                    JSONObject tmpElement = (JSONObject) element;
                    JSONObject firstName = (JSONObject) tmpElement.get("firstName");
                    if (firstName != null) {
                        firstNameValue = (String) firstName.get("value");
                    }
                }

                for (Object element : bindingsArray) {
                    JSONObject tmpElement = (JSONObject) element;
                    JSONObject lastName = (JSONObject) tmpElement.get("lastName");
                    if (lastName != null) {
                        lastNameValue = (String) lastName.get("value");
                    }
                }
                jsonOut.put("creator", firstNameValue + " " + lastNameValue);
                HashMap<String, String> hmBroader = new HashMap();
                for (Object element : bindingsArray) {
                    JSONObject tmpElement = (JSONObject) element;
                    JSONObject bpObj = (JSONObject) tmpElement.get("BroaderPreferred");
                    JSONObject bptObj = (JSONObject) tmpElement.get("BroaderPreferredTerm");
                    if (bpObj != null) {
                        String bp = (String) bpObj.get("value");
                        String bpt = (String) bptObj.get("value");
                        hmBroader.put(bpt, bp);
                    }
                }
                JSONArray tmpArrayBroader = new JSONArray();
                Iterator itB = hmBroader.entrySet().iterator();
                while (itB.hasNext()) {
                    Map.Entry pair = (Map.Entry) itB.next();
                    JSONObject tmpObject = new JSONObject();
                    tmpObject.put("label", pair.getKey());
                    tmpObject.put("uri", pair.getValue());
                    tmpArrayBroader.add(tmpObject);
                    itB.remove();
                }
                jsonOut.put("broaderTerms", tmpArrayBroader);
                HashMap<String, String> hmNarrower = new HashMap();
                for (Object element : bindingsArray) {
                    JSONObject tmpElement = (JSONObject) element;
                    JSONObject npObj = (JSONObject) tmpElement.get("NarrowerPreferred");
                    JSONObject nptObj = (JSONObject) tmpElement.get("NarrowerPreferredTerm");
                    if (npObj != null) {
                        String np = (String) npObj.get("value");
                        String npt = (String) nptObj.get("value");
                        hmNarrower.put(npt, np);
                    }
                }
                JSONArray tmpArrayNarrower = new JSONArray();
                Iterator itN = hmNarrower.entrySet().iterator();
                while (itN.hasNext()) {
                    Map.Entry pair = (Map.Entry) itN.next();
                    JSONObject tmpObject = new JSONObject();
                    tmpObject.put("label", pair.getKey());
                    tmpObject.put("uri", pair.getValue());
                    tmpArrayNarrower.add(tmpObject);
                    itN.remove();
                }
                jsonOut.put("narrowerTerms", tmpArrayNarrower);
                // get retcat info
                String type = "ls";
                String quality = "";
                String group = "";
                for (RetcatItem item : RetcatItems.getReferenceThesaurusCatalogue()) {
                    if (item.getType().equals(type)) {
                        quality = item.getQuality();
                        group = item.getGroup();
                    }
                }
                jsonOut.put("quality", quality);
                jsonOut.put("group", group);
                jsonOut.put("uri", url);
            } else {
                throw new ResourceNotAvailableException();
            }
            if (jsonOut.get("label") != null && !jsonOut.get("label").equals("")) {
                return jsonOut;
            } else {
                throw new RetcatException("no label for this uri available");
            }
        } catch (Exception e) {
            return new JSONObject();
        }
    }

}
