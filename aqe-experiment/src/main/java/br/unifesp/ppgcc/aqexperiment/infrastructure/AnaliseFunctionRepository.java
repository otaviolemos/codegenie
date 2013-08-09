package br.unifesp.ppgcc.aqexperiment.infrastructure;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.unifesp.ppgcc.aqexperiment.domain.AnaliseFunction;

@Repository("analiseFunctionRepository")
public class AnaliseFunctionRepository extends BaseRepository<AnaliseFunction>{

	public List<AnaliseFunction> findAllHardCode() throws Exception {
		List<AnaliseFunction> functions = new ArrayList<AnaliseFunction>();
		
		//functions.add(new AnaliseFunction("Blurring an image", 24, 11, 1, new Long[]{6020560l}));
		//functions.add(new AnaliseFunction("Capturing the screen into an image", 10, 8, 1, new Long[]{174422l}));
		functions.add(new AnaliseFunction("Computing the MD5 hash of a string", 16, 17, 4, new Long[]{5851102l, 5866342l, 5923691l}));
		functions.add(new AnaliseFunction("Converting byte arrays to hexadecimal strings", 19, 4, 2, new Long[]{163234l}));
		//functions.add(new AnaliseFunction("Converting hyphenated strings to camel case strings", 13, 20, 1, new Long[]{6002109l}));
		functions.add(new AnaliseFunction("Counting lines of a text file", 30, 12, 2, new Long[]{6045385l}));
		functions.add(new AnaliseFunction("Decoding a URL", 05, 14, 8, new Long[]{159513l, 159514l, 1447056l}));
		functions.add(new AnaliseFunction("Encoding Java strings for HTML displaying", 17 , 13, 13,  new Long[]{159521l, 159522l, 1447057l, 1460887l, 2519547l, 5841144l, 5851429l, 5858840l, 6021891l}));
		functions.add(new AnaliseFunction("Encrypting a password", 23, 2, 5, new Long[]{715811l, 1457869l, 5830408l, 5842071l, 5851101l, 5924962l}));
		functions.add(new AnaliseFunction("Filtering folder contents with specific file types", 20, 16 ,28, new Long[]{168156l, 168158l, 181537l, 181553l, 5889584l}));
		functions.add(new AnaliseFunction("Generating the complementary DNA seq.", 14, 9, 2, new Long[]{6035496l}));
		//functions.add(new AnaliseFunction("Generating the reverse complementary DNA seq.", 22, 10, 1, new Long[]{1366538l, 1447441l, 1683047l, 5877324l, 5924045l, 6042610l}));
		functions.add(new AnaliseFunction("Joining a list of strings in a single string", 01, 19, 4, new Long[]{5877324l, 1683047l, 5924045l, 1366538l, 1447441l, 6042610l}));
		//functions.add(new AnaliseFunction("Parsing a CSV file", 34, 21, 1, new Long[]{5986452l}));
		functions.add(new AnaliseFunction("Revert a text string", 35, 6, 2, new Long[]{1447523l, 6011188l}));
		functions.add(new AnaliseFunction("Rotating an image", 21, 3, 3, new Long[]{176148l, 180626l}));
		functions.add(new AnaliseFunction("Saving an image in JPG format", 11, 18, 3, new Long[]{1016680l, 3233932l, 6020216l}));
		functions.add(new AnaliseFunction("Scaling an image", 18, 1 , 8, new Long[]{176149l, 1447122l, 1472532l, 5854427l, 5861459l, 5895494l}));
		//functions.add(new AnaliseFunction("Sharpening an image", 9, 7, 1, new Long[]{6020559l}));
		functions.add(new AnaliseFunction("Unzipping files", 32, 5, 2, new Long[]{5923403l}));
		functions.add(new AnaliseFunction("Zipping files", 33, 15, 5, new Long[]{127109l, 5922767l, 6036870l, 6042875l}));
		
		return functions;
	}
}
