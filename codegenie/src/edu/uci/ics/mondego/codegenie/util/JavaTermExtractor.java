/**
 * @author <a href="sbajrach@ics.uci.edu">skb</a>
 *  created: May 13, 2007 
 */
package edu.uci.ics.mondego.codegenie.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Set;
import java.lang.Character;
import java.lang.StringBuffer;
import java.lang.String;

/**
 * Utility class to extract terms from Java FQNs or FQN fragments
 */
public class JavaTermExtractor {

	/**
	 * a valid whitespace as separator for terms
	 */
	public static String TERM_SEPARATOR = " ";

	public static String FQN_SPLIT_CHARS = "[^A-Za-z]";

	
	/**
	 * extracts the short name from FQN
	 * works both for fqn w/o method arguments or for
	 * fqn with method arguments
	 * @param fqn
	 * @return
	 */
	public static String extractShortName(String fqn){
		
		String[] _fragments = fqn.split("[.]");
		int _lastIndex = _fragments.length;
		String[] _lastFragment = _fragments[_lastIndex - 1].split("[(]");

		if (_lastFragment != null)
			return _lastFragment[0];
		else
			return "";
	}
	
	/**
	 * splits the fqn to fragments using "." as separator
	 * and returns a single string where all the fqn fragments
	 * are appended with TERM_SEPARATOR in between 
	 * @param fqn
	 * @return
	 */
	public static String extractFQNFragments(String fqn){
		return mergeTerms(fqn.split("[.]"));
	}
	
	/**
	 * Extracts the true FQN for a method, i.e; removes the method arguments
	 * 
	 * @param fqn input of form: org.foo.Cl.m(arg)
	 * @return method's fqn of form: orf.foo.Cl.m
	 */
	public static String removeMethodArguments(String fqn) {
		return fqn.split("[(]")[0];
	}

	/**
	 * @param fqn
	 * @return method arguments in format arg1,arg2,arg3 with no whitespaces
	 */
	public static String extractMethodArguments(String fqn) {

		String[] _fragments = fqn.split("[(]");
		
		if (_fragments == null || _fragments.length <= 1)
			return "";
		
		return _fragments[1].replaceAll("\\s", "").replace(")", "");

	}

	private static String getFQNFragmentTermsAsString(String fqnFragment) {

		SplitCamelCaseIdentifier splitter = new SplitCamelCaseIdentifier(
				fqnFragment);

		Collection<String> _strCol = splitter.split();

		return mergeTerms(_strCol);

	}

	/**
	 * Returns a single string representation of the FQN terms separated by
	 * TERM_SEPARATOR
	 * 
	 * @param fqn
	 *            Fully Qualified Name
	 * @return String representing the terms separated by TERM_SEPARATOR
	 */
	public static String getFQNTermsAsString(String fqn) {

		String[] fragments = fqn.split(FQN_SPLIT_CHARS);

		if (fragments == null || fragments.length <= 0)
			return "";

		Collection<String> _strCol = new LinkedList<String>();

		for (String _fragment : fragments) {
			_strCol.add(getFQNFragmentTermsAsString(_fragment));
		}

		return mergeTerms(_strCol);

	}

	/**
	 * Takes a Collection of String and returns a single string that has all the
	 * strings in the colletion separated with the TERM_SEPARATOR
	 * 
	 * @param strCol
	 * @return
	 */
	public static String mergeTerms(Collection<String> strCol) {

		String retVal = "";
		if (strCol == null || strCol.size() <= 0)
			return retVal;
		else {
			StringBuffer _buf = new StringBuffer();
			for (String s : strCol) {
				_buf.append(s);
				_buf.append(TERM_SEPARATOR);
			}
			retVal = _buf.toString().trim();
		}

		return retVal;
	}

	public static String mergeTerms(String[] strCol) {

		String retVal = "";
		if (strCol == null || strCol.length <= 0)
			return retVal;
		else {
			StringBuffer _buf = new StringBuffer();
			for (String s : strCol) {
				_buf.append(s);
				_buf.append(TERM_SEPARATOR);
			}
			retVal = _buf.toString().trim();
		}

		return retVal;
	}

}

/**
 * Sliced Document sliced from sourcerer, query: camel case split
 */
class SplitCamelCaseIdentifier {

	/* fields */
	private String ident;

	/* constructors */
	public SplitCamelCaseIdentifier(String ident) {
		this.ident = ident;
	}

	/* methods */
	public Collection<String> split() {
		String s = ident;
		Set<String> result = new HashSet<String>();

		while (s.length() > 0) {
			StringBuffer buf = new StringBuffer();

			char first = s.charAt(0);
			buf.append(first);
			int i = 1;

			if (s.length() > 1) {
				boolean camelWord;
				if (Character.isLowerCase(first)) {
					camelWord = true;
				} else {
					char next = s.charAt(i++);
					buf.append(next);
					camelWord = Character.isLowerCase(next);
				}

				while (i < s.length()) {
					char c = s.charAt(i);
					if (Character.isUpperCase(c)) {
						if (camelWord)
							break;
					} else if (!camelWord) {
						break;
					}
					buf.append(c);
					++i;
				}

				if (!camelWord && i < s.length()) {
					buf.deleteCharAt(buf.length() - 1);
					--i;
				}
			}

			result.add(buf.toString().toLowerCase(Locale.US));
			s = s.substring(i);
		}

		return result;
	}
}