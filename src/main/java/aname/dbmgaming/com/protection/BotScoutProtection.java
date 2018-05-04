package aname.dbmgaming.com.protection;

import aname.dbmgaming.com.util.WebsiteUtils;

public class BotScoutProtection {

    private String URL = "https://botscout.com/test/?ip=";

    public boolean isBotScoutBlacklisted(String ip) {
        return WebsiteUtils.getText(URL + ip).contains("Y");
    }

}
