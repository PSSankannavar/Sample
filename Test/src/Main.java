public class Main {
  public static void main(String[] argv) throws Exception {
    System.out.println(replace("Select distinct ORG_CLSFCTN_ID,ORG_CLSFCTN_NM from VW_SECTION_GPC WHERE ORG_STRUCTR_ID = ? Or ORG_CLSFCTN_CD = ?", "?", "1456"));
  }

  static String replace(String str, String pattern, String replace) {
    int start = 0;
    int index = 0;
    StringBuffer result = new StringBuffer();

    while ((index = str.indexOf(pattern, start)) >= 0) {
      result.append(str.substring(start, index));
      result.append(replace);
      start = index + pattern.length();
    }
    result.append(str.substring(start));
    return result.toString();
  }
}