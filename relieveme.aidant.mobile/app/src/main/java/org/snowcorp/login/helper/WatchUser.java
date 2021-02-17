package org.snowcorp.login.helper;

import java.util.Date;

public class WatchUser {
    private String UniqueCode;
    private String Name;
    private String FamilyLink;
    private Date BirthDate;

    public WatchUser(String uniqueCode) {
        UniqueCode = uniqueCode;
    }


    public String getUniqueCode() {
        return UniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        UniqueCode = uniqueCode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFamilyLink() {
        return FamilyLink;
    }

    public void setFamilyLink(String familyLink) {
        FamilyLink = familyLink;
    }

    public Date getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(Date birthDate) {
        BirthDate = birthDate;
    }
}
