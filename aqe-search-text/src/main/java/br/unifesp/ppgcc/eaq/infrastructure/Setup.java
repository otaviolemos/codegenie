package br.unifesp.ppgcc.eaq.infrastructure;

import java.util.ArrayList;
import java.util.List;

import br.unifesp.ppgcc.eaq.domain.SearchTerm;

public class Setup {

	private static List<SearchTerm> searchTerms = null;
	
	public static List<SearchTerm> getSearchTerms() {
		if(searchTerms != null)
			return searchTerms; 

		//loadSearchTerms();
		//loadSearchTerms2();
		loadSearchTerms3();

		return searchTerms;
	}
	
	private static void loadSearchTerms3(){
		searchTerms = new ArrayList<SearchTerm>();
		
		searchTerms.add(new SearchTerm("35", "Revert a text string", new String[]{"invert(","invertString(","stringInvert(","reverse(","reverseString(","stringReverse("}));
	}

	@SuppressWarnings("unused")
	private static void loadSearchTerms2(){
		searchTerms = new ArrayList<SearchTerm>();
		
		searchTerms.add(new SearchTerm("01", "Joining a list of strings in a single string", new String[]{"join(","merge(","uniteStrings(","joinStrings(","mergeStrings("}));
		searchTerms.add(new SearchTerm("02", "Trimming left spaces from a string", new String[]{"leftTrim(","trimLeading(","trimLead("}));
		searchTerms.add(new SearchTerm("03", "Extracting a file name from a full path", new String[]{"extractFile(","extractName(","pureFileName("}));
		searchTerms.add(new SearchTerm("04", "Computing the largest common prefix of two strings", new String[]{"lcp("}));
		searchTerms.add(new SearchTerm("05", "Decoding a URL", new String[]{"decodeURL("}));
		searchTerms.add(new SearchTerm("06", "Capitalizing first letters of a string", new String[]{"capitalizeWords("}));
		searchTerms.add(new SearchTerm("07", "Converting normal strings to hexadecimal strings", new String[]{"hex(","hexadecimal("}));
		searchTerms.add(new SearchTerm("08", "Removing carriage return / line feed from strings", new String[]{"removeCarriageReturn(","removeLineBreak(","removeLineBreaks(","deleteCarriage(","deleteCarriageReturns(","deleteLineBreak(","deleteLineBreaks("}));
		searchTerms.add(new SearchTerm("10", "Capturing the screen into an image", new String[]{"captureScreen("}));
		searchTerms.add(new SearchTerm("11", "Saving an image in JPG format", new String[]{"jpg(","jpeg("}));
		searchTerms.add(new SearchTerm("12", "Converting arabic numbers to alphanumerics", new String[]{"iToA("}));
		searchTerms.add(new SearchTerm("13", "Converting hyphenated strings to camel case strings", new String[]{"toCamelCase(","camelCase("}));
		searchTerms.add(new SearchTerm("14", "Generating the complementary DNA seq.", new String[]{"dna("}));
		searchTerms.add(new SearchTerm("15", "Converting camel case strings to phrases", new String[]{"camelToPhrase(","camelToSentence("}));
		searchTerms.add(new SearchTerm("16", "Computing the MD5 hash of a string", new String[]{"md5(","getMd5("}));
		searchTerms.add(new SearchTerm("17", "Encoding Java strings for HTML displaying", new String[]{"encodeURL("}));
		searchTerms.add(new SearchTerm("18", "Scaling an image", new String[]{"scaled(","resize("}));
		searchTerms.add(new SearchTerm("19", "Converting byte arrays to hexadecimal strings", new String[]{"toHex(","toHexadecimal(","hexadecimal("}));
		searchTerms.add(new SearchTerm("20", "Filtering folder contents with specific file types", new String[]{"filter("}));
		searchTerms.add(new SearchTerm("22", "Generating the reverse complementary DNA seq.", new String[]{"dna("}));
		searchTerms.add(new SearchTerm("23", "Encrypting a password", new String[]{"cypher(","cipher(","encode(","cryptograph("}));
		searchTerms.add(new SearchTerm("25", "Printing formatted strings for elapsed times given in ms", new String[]{"elapsed("}));
		searchTerms.add(new SearchTerm("27", "Converting hexadecimal strings to normal strings", new String[]{"hexadecimalToString("}));
		searchTerms.add(new SearchTerm("28", "Sorting objects using QuickSort", new String[]{"sort("}));
		searchTerms.add(new SearchTerm("29", "Computing the Easter holiday for a given year", new String[]{"easterSundayDay(","easterDay("}));
		searchTerms.add(new SearchTerm("30", "Counting lines of a text file", new String[]{"lineCount(","countLine("}));
		searchTerms.add(new SearchTerm("31", "Computing the Soundex hash of a string", new String[]{"sha("}));
		searchTerms.add(new SearchTerm("32", "Unzipping files", new String[]{"decompress(","extract("}));
		searchTerms.add(new SearchTerm("33", "Zipping files", new String[]{"zip(","compact("}));
		searchTerms.add(new SearchTerm("34", "Parsing a CSV file", new String[]{"csv("}));
	}

	@SuppressWarnings("unused")
	private static void loadSearchTerms(){

		searchTerms = new ArrayList<SearchTerm>();
		
		searchTerms.add(new SearchTerm("01", "Joining a list of strings in a single string", "unite("));
		searchTerms.add(new SearchTerm("02", "Trimming left spaces from a string", "trimLeft("));
		searchTerms.add(new SearchTerm("03", "Extracting a file name from a full path", "extractFileName("));
		searchTerms.add(new SearchTerm("04", "Computing the largest common prefix of two strings", "longestCommonPrefix("));
		searchTerms.add(new SearchTerm("05", "Decoding a URL", "decode("));
		searchTerms.add(new SearchTerm("06", "Capitalizing first letters of a string", "capitalizeFirst("));
		searchTerms.add(new SearchTerm("07", "Converting normal strings to hexadecimal strings", "hexToString("));
		searchTerms.add(new SearchTerm("08", "Removing carriage return / line feed from strings", "removeCarriage("));
		searchTerms.add(new SearchTerm("09", "Sharpening an image", "sharpen("));
		searchTerms.add(new SearchTerm("10", "Capturing the screen into an image", "capture("));
		searchTerms.add(new SearchTerm("11", "Saving an image in JPG format", "saveJpg("));
		searchTerms.add(new SearchTerm("12", "Converting arabic numbers to alphanumerics", "alpha("));
		searchTerms.add(new SearchTerm("13", "Converting hyphenated strings to camel case strings", "toCamel("));
		searchTerms.add(new SearchTerm("14", "Generating the complementary DNA seq.", "complement("));
		searchTerms.add(new SearchTerm("15", "Converting camel case strings to phrases", "camel("));
		searchTerms.add(new SearchTerm("16", "Computing the MD5 hash of a string", "getHash("));
		searchTerms.add(new SearchTerm("17", "Encoding Java strings for HTML displaying", "encode("));
		searchTerms.add(new SearchTerm("18", "Scaling an image", "scale("));
		searchTerms.add(new SearchTerm("19", "Converting byte arrays to hexadecimal strings", "toHexString("));
		searchTerms.add(new SearchTerm("20", "Filtering folder contents with specific file types", "fileList("));
		searchTerms.add(new SearchTerm("21", "Rotating an image", "rotate("));
		searchTerms.add(new SearchTerm("22", "Generating the reverse complementary DNA seq.", "reverseComplement("));
		searchTerms.add(new SearchTerm("23", "Encrypting a password", "crypt("));
		searchTerms.add(new SearchTerm("24", "Blurring an image", "blur("));
		searchTerms.add(new SearchTerm("25", "Printing formatted strings for elapsed times given in ms", "elapsedTime("));
		searchTerms.add(new SearchTerm("26", "Converting arabic numbers to roman numerals", "roman("));
		searchTerms.add(new SearchTerm("27", "Converting hexadecimal strings to normal strings", "HexToString("));
		searchTerms.add(new SearchTerm("28", "Sorting objects using QuickSort", "quickSort("));
		searchTerms.add(new SearchTerm("29", "Computing the Easter holiday for a given year", "easter("));
		searchTerms.add(new SearchTerm("30", "Counting lines of a text file", "countLines("));
		searchTerms.add(new SearchTerm("31", "Computing the Soundex hash of a string", "getShaHash("));
		searchTerms.add(new SearchTerm("32", "Unzipping files", "unzip("));
		searchTerms.add(new SearchTerm("33", "Zipping files", "compress("));
		searchTerms.add(new SearchTerm("34", "Parsing a CSV file", "parse("));
	}
}