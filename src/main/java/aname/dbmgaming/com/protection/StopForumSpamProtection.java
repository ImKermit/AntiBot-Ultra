package aname.dbmgaming.com.protection;

import aname.dbmgaming.com.util.WebsiteUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashSet;

public class StopForumSpamProtection {

    private HashSet<String> asnBlacklist = new HashSet<>();
    private HashSet<String> countryBlacklist = new HashSet<>();

    private String URL = "https://api.stopforumspam.com/api?ip=";

    private JsonParser parser = new JsonParser();

    public StopForumSpamProtection() {
    }

    public boolean isAsnBlacklisted(String asn) {
        return asnBlacklist.contains(asn);
    }

    public boolean isCountryBlacklisted(String country) {
        return countryBlacklist.contains(country);
    }

    public void addToAsnBlacklist(String asn) {
        asnBlacklist.add(asn);
    }

    public void addToCountryBlacklist(String country) {
        countryBlacklist.add(country);
    }

    public JsonObject getDataForIpAsJson(String ip) {
        String urlData = WebsiteUtils.getText(URL + ip + "&json");

        JsonElement tree = parser.parse(urlData);
        JsonObject object = tree.getAsJsonObject();
        JsonElement element = object.get("ip");
        return element.getAsJsonObject();
    }

    public boolean isStopForumSpamBlacklisted(String ip) {
        return WebsiteUtils.getText(URL + ip).contains("yes");
    }

}
