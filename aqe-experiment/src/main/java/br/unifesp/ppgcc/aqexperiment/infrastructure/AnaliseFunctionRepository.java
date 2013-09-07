package br.unifesp.ppgcc.aqexperiment.infrastructure;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.unifesp.ppgcc.aqexperiment.domain.AnaliseFunction;
import br.unifesp.ppgcc.aqexperiment.infrastructure.util.ConfigProperties;

@Repository("analiseFunctionRepository")
public class AnaliseFunctionRepository extends BaseRepository<AnaliseFunction>{

	public List<AnaliseFunction> findAllValides() throws Exception {
		List<AnaliseFunction> analiseFunctions = new ArrayList<AnaliseFunction>();
		for(AnaliseFunction analiseFunction : this.findAllHardCode()){
			if(!this.isValidFunction(analiseFunction))
				continue;
			analiseFunctions.add(analiseFunction);
		}
		return analiseFunctions;
	}

	private boolean isValidFunction(AnaliseFunction analiseFunction) throws Exception {
		if(new Boolean(ConfigProperties.getProperty("aqExperiment.moreOneRelevant")) && analiseFunction.getRelevantsSolrIds().length <= 1)
			return false;
		return true;
	}
	
	private List<AnaliseFunction> findAllHardCode() throws Exception {
		List<AnaliseFunction> functions = new ArrayList<AnaliseFunction>();
		
		functions.add(new AnaliseFunction(1, "Scaling an image", 18, 8, new Long[]{176149l, 1447122l, 1472532l, 5854427l, 5861459l, 5895494l}));
		functions.add(new AnaliseFunction(2, "Encrypting a password", 23, 5, new Long[]{715811l, 1457869l, 5830408l, 5842071l, 5851101l, 5924962l}));
		functions.add(new AnaliseFunction(3, "Rotating an image", 21, 3, new Long[]{176148l, 180626l}));
		functions.add(new AnaliseFunction(4, "Converting byte arrays to hexadecimal strings", 19, 2, new Long[]{163234l}));
		functions.add(new AnaliseFunction(5, "Unzipping files", 32, 2, new Long[]{5923403l}));
		functions.add(new AnaliseFunction(6, "Revert a text string", 35, 2, new Long[]{1447523l, 6011188l}));
		functions.add(new AnaliseFunction(7, "Sharpening an image", 9, 1, new Long[]{6020559l}));
		functions.add(new AnaliseFunction(8, "Capturing the screen into an image", 10, 1, new Long[]{174422l}));
		functions.add(new AnaliseFunction(9, "Generating the complementary DNA seq.", 14, 2, new Long[]{6035496l}));
		functions.add(new AnaliseFunction(10, "Generating the reverse complementary DNA seq.", 22, 1, new Long[]{1366538l, 1447441l, 1683047l, 5877324l, 5924045l, 6042610l}));
		functions.add(new AnaliseFunction(11, "Blurring an image", 24, 1, new Long[]{6020560l}));
		functions.add(new AnaliseFunction(12, "Counting lines of a text file", 30, 2, new Long[]{6045385l}));
		functions.add(new AnaliseFunction(13, "Encoding Java strings for HTML displaying", 17 , 13,  new Long[]{159521l, 159522l, 1447057l, 1460887l, 2519547l, 5841144l, 5851429l, 5858840l, 6021891l}));
		functions.add(new AnaliseFunction(14, "Decoding a URL", 05, 8, new Long[]{159513l, 159514l, 1447056l}));
		functions.add(new AnaliseFunction(15, "Parsing a CSV file", 34, 1, new Long[]{5986452l}));
		functions.add(new AnaliseFunction(16, "Zipping files", 33, 5, new Long[]{127109l, 5922767l, 6036870l, 6042875l}));
		functions.add(new AnaliseFunction(17, "Filtering folder contents with specific file types", 20, 28, new Long[]{168156l, 168158l, 181537l, 181553l, 5889584l}));
		functions.add(new AnaliseFunction(18, "Computing the MD5 hash of a string", 16, 4, new Long[]{5851102l, 5866342l, 5923691l}));
		functions.add(new AnaliseFunction(19, "Saving an image in JPG format", 11, 3, new Long[]{1016680l, 3233932l, 6020216l}));
		functions.add(new AnaliseFunction(20, "Joining a list of strings in a single string", 1, 4, new Long[]{5877324l, 1683047l, 5924045l, 1366538l, 1447441l, 6042610l}));
		functions.add(new AnaliseFunction(21, "Converting hyphenated strings to camel case strings", 13, 1, new Long[]{6002109l}));
		
		return functions;
	}
}
