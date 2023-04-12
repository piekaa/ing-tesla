package pl.piekoszek.ing.json;

import java.io.InputStream;
import java.util.List;

record Formatting(List<String> fields,
                  int bytesBetweenObjectOpenAndFirstNameChar,
                  int bytesBetweenLastNameCharAndFirstValueToken,
                  int bytesBetweenLastValueTokenAndComma,
                  int bytesBetweenLastValueTokenAndFirstNameChar,
                  int bytesBetweenLastObjectValueTokenAndAfterNextObjectOpen,
                  InputStream usedData) {

}
