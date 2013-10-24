package br.unifesp.ppgcc.aqexperiment.infrastructure.util;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import br.unifesp.ppgcc.aqexperiment.domain.helper.TagCloudMutantQuery;
import br.unifesp.ppgcc.aqexperiment.domain.helper.TagCloudWord;

public class TagCloudHelperTest {
	
	@Test
	public void getMethodNameWords(){
		String[] names = TagCloudHelper.getMethodNameWords("toHexString");
		assertTrue(names.length == 3);
		assertTrue(names[0].equals("to"));
		assertTrue(names[1].equals("hex"));
		assertTrue(names[2].equals("string"));

		names = TagCloudHelper.getMethodNameWords("to");
		assertTrue(names.length == 1);
		
		names = TagCloudHelper.getMethodNameWords("");
		assertTrue(names.length == 0);
	}
	
	@Test
	public void getSynonymsTest() throws Exception {
		assertTrue(TagCloudHelper.getSynonyms("String").size() == 13);
	}
	
	@Test
	public void getTagCloudFrequencies() throws Exception{
		List<TagCloudWord> tagCloudWords = TagCloudHelper.getTagCloudWordFrequencies("String");
		assertTrue(tagCloudWords.size() == 4);
		assertTrue(tagCloudWords.get(0).isOriginalWord());
		assertTrue(tagCloudWords.get(0).getFrequency() == 47506);
		assertTrue(tagCloudWords.get(1).getFrequency() == 2736);
		assertTrue(tagCloudWords.get(2).getFrequency() == 2598);
		assertTrue(tagCloudWords.get(3).getFrequency() == 820);

		tagCloudWords = TagCloudHelper.getTagCloudWordFrequencies("invert");
		assertTrue(tagCloudWords.size() == 3);
		assertTrue(tagCloudWords.get(0).isOriginalWord());
		assertTrue(tagCloudWords.get(0).getFrequency() == 150);
		assertTrue(tagCloudWords.get(1).getFrequency() == 826);
		assertTrue(tagCloudWords.get(2).getFrequency() == 0);
		
		tagCloudWords = TagCloudHelper.getTagCloudWordFrequencies("compress");
		assertTrue(tagCloudWords.size() == 4);
		assertTrue(tagCloudWords.get(0).isOriginalWord());
		assertTrue(tagCloudWords.get(0).getFrequency() == 305);
		assertTrue(tagCloudWords.get(1).getFrequency() == 600);
		assertTrue(tagCloudWords.get(2).getFrequency() == 56);
		assertTrue(tagCloudWords.get(3).getFrequency() == 50);

		tagCloudWords = TagCloudHelper.getTagCloudWordFrequencies("zip");
		assertTrue(tagCloudWords.size() == 4);
		assertTrue(tagCloudWords.get(0).isOriginalWord());
		assertTrue(tagCloudWords.get(0).getFrequency() == 365);
		assertTrue(tagCloudWords.get(1).getFrequency() == 6039);
		assertTrue(tagCloudWords.get(2).getFrequency() == 543);
		assertTrue(tagCloudWords.get(3).getFrequency() == 429);
	}
	
	@Test
	public void getFrequecy() throws Exception {
		assertTrue(TagCloudHelper.getFrequecy("to") == 59820);
		assertTrue(TagCloudHelper.getFrequecy("Hex") == 1022);
		assertTrue(TagCloudHelper.getFrequecy("String") == 47506);
	}

	@Test
	public void getAllWordsList() throws Exception {
		List<List<TagCloudWord>> allWordsList = TagCloudHelper.getAllWordsList("stringZipCompressInvert");
		assertTrue(allWordsList.size() == 4);
		assertTrue(allWordsList.get(0).size() == 4);
		assertTrue(allWordsList.get(1).size() == 4);
		assertTrue(allWordsList.get(2).size() == 4);
		assertTrue(allWordsList.get(3).size() == 3);

		//String
		assertTrue(allWordsList.get(0).get(0).getWord().equals("String"));
		assertTrue(allWordsList.get(0).get(1).getWord().equals("Thread"));
		assertTrue(allWordsList.get(0).get(2).getWord().equals("Draw"));
		assertTrue(allWordsList.get(0).get(3).getWord().equals("Chain"));
		//Zip
		assertTrue(allWordsList.get(1).get(0).getWord().equals("Zip"));
		assertTrue(allWordsList.get(1).get(1).getWord().equals("Null"));
		assertTrue(allWordsList.get(1).get(2).getWord().equals("Zero"));
		assertTrue(allWordsList.get(1).get(3).getWord().equals("Cipher"));
		//Compress
		assertTrue(allWordsList.get(2).get(0).getWord().equals("Compress"));
		assertTrue(allWordsList.get(2).get(1).getWord().equals("Compact"));
		assertTrue(allWordsList.get(2).get(2).getWord().equals("Contract"));
		assertTrue(allWordsList.get(2).get(3).getWord().equals("Squeeze"));
		//Invert
		assertTrue(allWordsList.get(3).get(0).getWord().equals("Invert"));
		assertTrue(allWordsList.get(3).get(1).getWord().equals("Reverse"));
		assertTrue(allWordsList.get(3).get(2).getWord().equals("TurnBack"));
	}
	
	//@Test
	public void getTagCloudMutantQueries1()throws Exception{
		List<List<TagCloudWord>> allWordsList = TagCloudHelper.getAllWordsList("string");
		List<TagCloudMutantQuery> mutantQueries = TagCloudHelper.getTagCloudMutantQueries(allWordsList);
		assertTrue(mutantQueries.size() == 3);
		assertTrue(mutantQueries.get(0).getMutantMethodName().equals("String"));
		assertTrue(mutantQueries.get(1).getMutantMethodName().equals("Thread"));
		assertTrue(mutantQueries.get(2).getMutantMethodName().equals("Draw"));
		assertTrue(mutantQueries.get(3).getMutantMethodName().equals("Chain"));
	}

	//@Test
	public void getTagCloudMutantQueries2()throws Exception{
		List<List<TagCloudWord>> allWordsList = TagCloudHelper.getAllWordsList("stringZip");
		List<TagCloudMutantQuery> mutantQueries = TagCloudHelper.getTagCloudMutantQueries(allWordsList);
		assertTrue(mutantQueries.size() == 15);
		assertTrue(mutantQueries.get(0).getMutantMethodName().equals("StringZip"));
		assertTrue(mutantQueries.get(1).getMutantMethodName().equals("StringNull"));
		assertTrue(mutantQueries.get(2).getMutantMethodName().equals("StringZero"));
		assertTrue(mutantQueries.get(3).getMutantMethodName().equals("StringCipher"));
		assertTrue(mutantQueries.get(4).getMutantMethodName().equals("ThreadZip"));
		assertTrue(mutantQueries.get(5).getMutantMethodName().equals("ThreadNull"));
		assertTrue(mutantQueries.get(6).getMutantMethodName().equals("ThreadZero"));
		assertTrue(mutantQueries.get(7).getMutantMethodName().equals("ThreadCipher"));
		assertTrue(mutantQueries.get(8).getMutantMethodName().equals("DrawZip"));
		assertTrue(mutantQueries.get(9).getMutantMethodName().equals("DrawNull"));
		assertTrue(mutantQueries.get(10).getMutantMethodName().equals("DrawZero"));
		assertTrue(mutantQueries.get(11).getMutantMethodName().equals("DrawCipher"));
		assertTrue(mutantQueries.get(12).getMutantMethodName().equals("ChainZip"));
		assertTrue(mutantQueries.get(13).getMutantMethodName().equals("ChainNull"));
		assertTrue(mutantQueries.get(14).getMutantMethodName().equals("ChainZero"));
		assertTrue(mutantQueries.get(15).getMutantMethodName().equals("ChainCipher"));
	}

	@Test
	public void getTagCloudMutantQueries4()throws Exception{
		List<List<TagCloudWord>> allWordsList = TagCloudHelper.getAllWordsList("StringZipCompressInvert");
		List<TagCloudMutantQuery> mutantQueries = TagCloudHelper.getTagCloudMutantQueries(allWordsList);
		assertTrue(mutantQueries.size() == 191);
		assertTrue(mutantQueries.get(0).getMutantMethodName().equals("StringZipCompressReverse"));
		assertTrue(mutantQueries.get(1).getMutantMethodName().equals("StringZipCompressTurnBack"));
		assertTrue(mutantQueries.get(2).getMutantMethodName().equals("StringZipCompactInvert"));
		assertTrue(mutantQueries.get(3).getMutantMethodName().equals("StringZipCompactReverse"));
		assertTrue(mutantQueries.get(4).getMutantMethodName().equals("StringZipCompactTurnBack"));
		assertTrue(mutantQueries.get(5).getMutantMethodName().equals("StringZipContractInvert"));
		assertTrue(mutantQueries.get(6).getMutantMethodName().equals("StringZipContractReverse"));
		assertTrue(mutantQueries.get(7).getMutantMethodName().equals("StringZipContractTurnBack"));
		assertTrue(mutantQueries.get(8).getMutantMethodName().equals("StringZipSqueezeInvert"));
		assertTrue(mutantQueries.get(9).getMutantMethodName().equals("StringZipSqueezeReverse"));
		assertTrue(mutantQueries.get(10).getMutantMethodName().equals("StringZipSqueezeTurnBack"));
		assertTrue(mutantQueries.get(11).getMutantMethodName().equals("StringNullCompressInvert"));
		assertTrue(mutantQueries.get(12).getMutantMethodName().equals("StringNullCompressReverse"));
		assertTrue(mutantQueries.get(13).getMutantMethodName().equals("StringNullCompressTurnBack"));
		assertTrue(mutantQueries.get(14).getMutantMethodName().equals("StringNullCompactInvert"));
		assertTrue(mutantQueries.get(15).getMutantMethodName().equals("StringNullCompactReverse"));
		assertTrue(mutantQueries.get(16).getMutantMethodName().equals("StringNullCompactTurnBack"));
		assertTrue(mutantQueries.get(17).getMutantMethodName().equals("StringNullContractInvert"));
		assertTrue(mutantQueries.get(18).getMutantMethodName().equals("StringNullContractReverse"));
		assertTrue(mutantQueries.get(19).getMutantMethodName().equals("StringNullContractTurnBack"));
		assertTrue(mutantQueries.get(20).getMutantMethodName().equals("StringNullSqueezeInvert"));
		assertTrue(mutantQueries.get(21).getMutantMethodName().equals("StringNullSqueezeReverse"));
		assertTrue(mutantQueries.get(22).getMutantMethodName().equals("StringNullSqueezeTurnBack"));
		assertTrue(mutantQueries.get(23).getMutantMethodName().equals("StringZeroCompressInvert"));
		assertTrue(mutantQueries.get(24).getMutantMethodName().equals("StringZeroCompressReverse"));
		assertTrue(mutantQueries.get(25).getMutantMethodName().equals("StringZeroCompressTurnBack"));
		assertTrue(mutantQueries.get(26).getMutantMethodName().equals("StringZeroCompactInvert"));
		assertTrue(mutantQueries.get(27).getMutantMethodName().equals("StringZeroCompactReverse"));
		assertTrue(mutantQueries.get(28).getMutantMethodName().equals("StringZeroCompactTurnBack"));
		assertTrue(mutantQueries.get(29).getMutantMethodName().equals("StringZeroContractInvert"));
		assertTrue(mutantQueries.get(30).getMutantMethodName().equals("StringZeroContractReverse"));
		assertTrue(mutantQueries.get(31).getMutantMethodName().equals("StringZeroContractTurnBack"));
		assertTrue(mutantQueries.get(32).getMutantMethodName().equals("StringZeroSqueezeInvert"));
		assertTrue(mutantQueries.get(33).getMutantMethodName().equals("StringZeroSqueezeReverse"));
		assertTrue(mutantQueries.get(34).getMutantMethodName().equals("StringZeroSqueezeTurnBack"));
		assertTrue(mutantQueries.get(35).getMutantMethodName().equals("StringCipherCompressInvert"));
		assertTrue(mutantQueries.get(36).getMutantMethodName().equals("StringCipherCompressReverse"));
		assertTrue(mutantQueries.get(37).getMutantMethodName().equals("StringCipherCompressTurnBack"));
		assertTrue(mutantQueries.get(38).getMutantMethodName().equals("StringCipherCompactInvert"));
		assertTrue(mutantQueries.get(39).getMutantMethodName().equals("StringCipherCompactReverse"));
		assertTrue(mutantQueries.get(40).getMutantMethodName().equals("StringCipherCompactTurnBack"));
		assertTrue(mutantQueries.get(41).getMutantMethodName().equals("StringCipherContractInvert"));
		assertTrue(mutantQueries.get(42).getMutantMethodName().equals("StringCipherContractReverse"));
		assertTrue(mutantQueries.get(43).getMutantMethodName().equals("StringCipherContractTurnBack"));
		assertTrue(mutantQueries.get(44).getMutantMethodName().equals("StringCipherSqueezeInvert"));
		assertTrue(mutantQueries.get(45).getMutantMethodName().equals("StringCipherSqueezeReverse"));
		assertTrue(mutantQueries.get(46).getMutantMethodName().equals("StringCipherSqueezeTurnBack"));
		assertTrue(mutantQueries.get(47).getMutantMethodName().equals("ThreadZipCompressInvert"));
		assertTrue(mutantQueries.get(48).getMutantMethodName().equals("ThreadZipCompressReverse"));
		assertTrue(mutantQueries.get(49).getMutantMethodName().equals("ThreadZipCompressTurnBack"));
		assertTrue(mutantQueries.get(50).getMutantMethodName().equals("ThreadZipCompactInvert"));
		assertTrue(mutantQueries.get(51).getMutantMethodName().equals("ThreadZipCompactReverse"));
		assertTrue(mutantQueries.get(52).getMutantMethodName().equals("ThreadZipCompactTurnBack"));
		assertTrue(mutantQueries.get(53).getMutantMethodName().equals("ThreadZipContractInvert"));
		assertTrue(mutantQueries.get(54).getMutantMethodName().equals("ThreadZipContractReverse"));
		assertTrue(mutantQueries.get(55).getMutantMethodName().equals("ThreadZipContractTurnBack"));
		assertTrue(mutantQueries.get(56).getMutantMethodName().equals("ThreadZipSqueezeInvert"));
		assertTrue(mutantQueries.get(57).getMutantMethodName().equals("ThreadZipSqueezeReverse"));
		assertTrue(mutantQueries.get(58).getMutantMethodName().equals("ThreadZipSqueezeTurnBack"));
		assertTrue(mutantQueries.get(59).getMutantMethodName().equals("ThreadNullCompressInvert"));
		assertTrue(mutantQueries.get(60).getMutantMethodName().equals("ThreadNullCompressReverse"));
		assertTrue(mutantQueries.get(61).getMutantMethodName().equals("ThreadNullCompressTurnBack"));
		assertTrue(mutantQueries.get(62).getMutantMethodName().equals("ThreadNullCompactInvert"));
		assertTrue(mutantQueries.get(63).getMutantMethodName().equals("ThreadNullCompactReverse"));
		assertTrue(mutantQueries.get(64).getMutantMethodName().equals("ThreadNullCompactTurnBack"));
		assertTrue(mutantQueries.get(65).getMutantMethodName().equals("ThreadNullContractInvert"));
		assertTrue(mutantQueries.get(66).getMutantMethodName().equals("ThreadNullContractReverse"));
		assertTrue(mutantQueries.get(67).getMutantMethodName().equals("ThreadNullContractTurnBack"));
		assertTrue(mutantQueries.get(68).getMutantMethodName().equals("ThreadNullSqueezeInvert"));
		assertTrue(mutantQueries.get(69).getMutantMethodName().equals("ThreadNullSqueezeReverse"));
		assertTrue(mutantQueries.get(70).getMutantMethodName().equals("ThreadNullSqueezeTurnBack"));
		assertTrue(mutantQueries.get(71).getMutantMethodName().equals("ThreadZeroCompressInvert"));
		assertTrue(mutantQueries.get(72).getMutantMethodName().equals("ThreadZeroCompressReverse"));
		assertTrue(mutantQueries.get(73).getMutantMethodName().equals("ThreadZeroCompressTurnBack"));
		assertTrue(mutantQueries.get(74).getMutantMethodName().equals("ThreadZeroCompactInvert"));
		assertTrue(mutantQueries.get(75).getMutantMethodName().equals("ThreadZeroCompactReverse"));
		assertTrue(mutantQueries.get(76).getMutantMethodName().equals("ThreadZeroCompactTurnBack"));
		assertTrue(mutantQueries.get(77).getMutantMethodName().equals("ThreadZeroContractInvert"));
		assertTrue(mutantQueries.get(78).getMutantMethodName().equals("ThreadZeroContractReverse"));
		assertTrue(mutantQueries.get(79).getMutantMethodName().equals("ThreadZeroContractTurnBack"));
		assertTrue(mutantQueries.get(80).getMutantMethodName().equals("ThreadZeroSqueezeInvert"));
		assertTrue(mutantQueries.get(81).getMutantMethodName().equals("ThreadZeroSqueezeReverse"));
		assertTrue(mutantQueries.get(82).getMutantMethodName().equals("ThreadZeroSqueezeTurnBack"));
		assertTrue(mutantQueries.get(83).getMutantMethodName().equals("ThreadCipherCompressInvert"));
		assertTrue(mutantQueries.get(84).getMutantMethodName().equals("ThreadCipherCompressReverse"));
		assertTrue(mutantQueries.get(85).getMutantMethodName().equals("ThreadCipherCompressTurnBack"));
		assertTrue(mutantQueries.get(86).getMutantMethodName().equals("ThreadCipherCompactInvert"));
		assertTrue(mutantQueries.get(87).getMutantMethodName().equals("ThreadCipherCompactReverse"));
		assertTrue(mutantQueries.get(88).getMutantMethodName().equals("ThreadCipherCompactTurnBack"));
		assertTrue(mutantQueries.get(89).getMutantMethodName().equals("ThreadCipherContractInvert"));
		assertTrue(mutantQueries.get(90).getMutantMethodName().equals("ThreadCipherContractReverse"));
		assertTrue(mutantQueries.get(91).getMutantMethodName().equals("ThreadCipherContractTurnBack"));
		assertTrue(mutantQueries.get(92).getMutantMethodName().equals("ThreadCipherSqueezeInvert"));
		assertTrue(mutantQueries.get(93).getMutantMethodName().equals("ThreadCipherSqueezeReverse"));
		assertTrue(mutantQueries.get(94).getMutantMethodName().equals("ThreadCipherSqueezeTurnBack"));
		assertTrue(mutantQueries.get(95).getMutantMethodName().equals("DrawZipCompressInvert"));
		assertTrue(mutantQueries.get(96).getMutantMethodName().equals("DrawZipCompressReverse"));
		assertTrue(mutantQueries.get(97).getMutantMethodName().equals("DrawZipCompressTurnBack"));
		assertTrue(mutantQueries.get(98).getMutantMethodName().equals("DrawZipCompactInvert"));
		assertTrue(mutantQueries.get(99).getMutantMethodName().equals("DrawZipCompactReverse"));
		assertTrue(mutantQueries.get(100).getMutantMethodName().equals("DrawZipCompactTurnBack"));
		assertTrue(mutantQueries.get(101).getMutantMethodName().equals("DrawZipContractInvert"));
		assertTrue(mutantQueries.get(102).getMutantMethodName().equals("DrawZipContractReverse"));
		assertTrue(mutantQueries.get(103).getMutantMethodName().equals("DrawZipContractTurnBack"));
		assertTrue(mutantQueries.get(104).getMutantMethodName().equals("DrawZipSqueezeInvert"));
		assertTrue(mutantQueries.get(105).getMutantMethodName().equals("DrawZipSqueezeReverse"));
		assertTrue(mutantQueries.get(106).getMutantMethodName().equals("DrawZipSqueezeTurnBack"));
		assertTrue(mutantQueries.get(107).getMutantMethodName().equals("DrawNullCompressInvert"));
		assertTrue(mutantQueries.get(108).getMutantMethodName().equals("DrawNullCompressReverse"));
		assertTrue(mutantQueries.get(109).getMutantMethodName().equals("DrawNullCompressTurnBack"));
		assertTrue(mutantQueries.get(110).getMutantMethodName().equals("DrawNullCompactInvert"));
		assertTrue(mutantQueries.get(111).getMutantMethodName().equals("DrawNullCompactReverse"));
		assertTrue(mutantQueries.get(112).getMutantMethodName().equals("DrawNullCompactTurnBack"));
		assertTrue(mutantQueries.get(113).getMutantMethodName().equals("DrawNullContractInvert"));
		assertTrue(mutantQueries.get(114).getMutantMethodName().equals("DrawNullContractReverse"));
		assertTrue(mutantQueries.get(115).getMutantMethodName().equals("DrawNullContractTurnBack"));
		assertTrue(mutantQueries.get(116).getMutantMethodName().equals("DrawNullSqueezeInvert"));
		assertTrue(mutantQueries.get(117).getMutantMethodName().equals("DrawNullSqueezeReverse"));
		assertTrue(mutantQueries.get(118).getMutantMethodName().equals("DrawNullSqueezeTurnBack"));
		assertTrue(mutantQueries.get(119).getMutantMethodName().equals("DrawZeroCompressInvert"));
		assertTrue(mutantQueries.get(120).getMutantMethodName().equals("DrawZeroCompressReverse"));
		assertTrue(mutantQueries.get(121).getMutantMethodName().equals("DrawZeroCompressTurnBack"));
		assertTrue(mutantQueries.get(122).getMutantMethodName().equals("DrawZeroCompactInvert"));
		assertTrue(mutantQueries.get(123).getMutantMethodName().equals("DrawZeroCompactReverse"));
		assertTrue(mutantQueries.get(124).getMutantMethodName().equals("DrawZeroCompactTurnBack"));
		assertTrue(mutantQueries.get(125).getMutantMethodName().equals("DrawZeroContractInvert"));
		assertTrue(mutantQueries.get(126).getMutantMethodName().equals("DrawZeroContractReverse"));
		assertTrue(mutantQueries.get(127).getMutantMethodName().equals("DrawZeroContractTurnBack"));
		assertTrue(mutantQueries.get(128).getMutantMethodName().equals("DrawZeroSqueezeInvert"));
		assertTrue(mutantQueries.get(129).getMutantMethodName().equals("DrawZeroSqueezeReverse"));
		assertTrue(mutantQueries.get(130).getMutantMethodName().equals("DrawZeroSqueezeTurnBack"));
		assertTrue(mutantQueries.get(131).getMutantMethodName().equals("DrawCipherCompressInvert"));
		assertTrue(mutantQueries.get(132).getMutantMethodName().equals("DrawCipherCompressReverse"));
		assertTrue(mutantQueries.get(133).getMutantMethodName().equals("DrawCipherCompressTurnBack"));
		assertTrue(mutantQueries.get(134).getMutantMethodName().equals("DrawCipherCompactInvert"));
		assertTrue(mutantQueries.get(135).getMutantMethodName().equals("DrawCipherCompactReverse"));
		assertTrue(mutantQueries.get(136).getMutantMethodName().equals("DrawCipherCompactTurnBack"));
		assertTrue(mutantQueries.get(137).getMutantMethodName().equals("DrawCipherContractInvert"));
		assertTrue(mutantQueries.get(138).getMutantMethodName().equals("DrawCipherContractReverse"));
		assertTrue(mutantQueries.get(139).getMutantMethodName().equals("DrawCipherContractTurnBack"));
		assertTrue(mutantQueries.get(140).getMutantMethodName().equals("DrawCipherSqueezeInvert"));
		assertTrue(mutantQueries.get(141).getMutantMethodName().equals("DrawCipherSqueezeReverse"));
		assertTrue(mutantQueries.get(142).getMutantMethodName().equals("DrawCipherSqueezeTurnBack"));
		assertTrue(mutantQueries.get(143).getMutantMethodName().equals("ChainZipCompressInvert"));
		assertTrue(mutantQueries.get(144).getMutantMethodName().equals("ChainZipCompressReverse"));
		assertTrue(mutantQueries.get(145).getMutantMethodName().equals("ChainZipCompressTurnBack"));
		assertTrue(mutantQueries.get(146).getMutantMethodName().equals("ChainZipCompactInvert"));
		assertTrue(mutantQueries.get(147).getMutantMethodName().equals("ChainZipCompactReverse"));
		assertTrue(mutantQueries.get(148).getMutantMethodName().equals("ChainZipCompactTurnBack"));
		assertTrue(mutantQueries.get(149).getMutantMethodName().equals("ChainZipContractInvert"));
		assertTrue(mutantQueries.get(150).getMutantMethodName().equals("ChainZipContractReverse"));
		assertTrue(mutantQueries.get(151).getMutantMethodName().equals("ChainZipContractTurnBack"));
		assertTrue(mutantQueries.get(152).getMutantMethodName().equals("ChainZipSqueezeInvert"));
		assertTrue(mutantQueries.get(153).getMutantMethodName().equals("ChainZipSqueezeReverse"));
		assertTrue(mutantQueries.get(154).getMutantMethodName().equals("ChainZipSqueezeTurnBack"));
		assertTrue(mutantQueries.get(155).getMutantMethodName().equals("ChainNullCompressInvert"));
		assertTrue(mutantQueries.get(156).getMutantMethodName().equals("ChainNullCompressReverse"));
		assertTrue(mutantQueries.get(157).getMutantMethodName().equals("ChainNullCompressTurnBack"));
		assertTrue(mutantQueries.get(158).getMutantMethodName().equals("ChainNullCompactInvert"));
		assertTrue(mutantQueries.get(159).getMutantMethodName().equals("ChainNullCompactReverse"));
		assertTrue(mutantQueries.get(160).getMutantMethodName().equals("ChainNullCompactTurnBack"));
		assertTrue(mutantQueries.get(161).getMutantMethodName().equals("ChainNullContractInvert"));
		assertTrue(mutantQueries.get(162).getMutantMethodName().equals("ChainNullContractReverse"));
		assertTrue(mutantQueries.get(163).getMutantMethodName().equals("ChainNullContractTurnBack"));
		assertTrue(mutantQueries.get(164).getMutantMethodName().equals("ChainNullSqueezeInvert"));
		assertTrue(mutantQueries.get(165).getMutantMethodName().equals("ChainNullSqueezeReverse"));
		assertTrue(mutantQueries.get(166).getMutantMethodName().equals("ChainNullSqueezeTurnBack"));
		assertTrue(mutantQueries.get(167).getMutantMethodName().equals("ChainZeroCompressInvert"));
		assertTrue(mutantQueries.get(168).getMutantMethodName().equals("ChainZeroCompressReverse"));
		assertTrue(mutantQueries.get(169).getMutantMethodName().equals("ChainZeroCompressTurnBack"));
		assertTrue(mutantQueries.get(170).getMutantMethodName().equals("ChainZeroCompactInvert"));
		assertTrue(mutantQueries.get(171).getMutantMethodName().equals("ChainZeroCompactReverse"));
		assertTrue(mutantQueries.get(172).getMutantMethodName().equals("ChainZeroCompactTurnBack"));
		assertTrue(mutantQueries.get(173).getMutantMethodName().equals("ChainZeroContractInvert"));
		assertTrue(mutantQueries.get(174).getMutantMethodName().equals("ChainZeroContractReverse"));
		assertTrue(mutantQueries.get(175).getMutantMethodName().equals("ChainZeroContractTurnBack"));
		assertTrue(mutantQueries.get(176).getMutantMethodName().equals("ChainZeroSqueezeInvert"));
		assertTrue(mutantQueries.get(177).getMutantMethodName().equals("ChainZeroSqueezeReverse"));
		assertTrue(mutantQueries.get(178).getMutantMethodName().equals("ChainZeroSqueezeTurnBack"));
		assertTrue(mutantQueries.get(179).getMutantMethodName().equals("ChainCipherCompressInvert"));
		assertTrue(mutantQueries.get(180).getMutantMethodName().equals("ChainCipherCompressReverse"));
		assertTrue(mutantQueries.get(181).getMutantMethodName().equals("ChainCipherCompressTurnBack"));
		assertTrue(mutantQueries.get(182).getMutantMethodName().equals("ChainCipherCompactInvert"));
		assertTrue(mutantQueries.get(183).getMutantMethodName().equals("ChainCipherCompactReverse"));
		assertTrue(mutantQueries.get(184).getMutantMethodName().equals("ChainCipherCompactTurnBack"));
		assertTrue(mutantQueries.get(185).getMutantMethodName().equals("ChainCipherContractInvert"));
		assertTrue(mutantQueries.get(186).getMutantMethodName().equals("ChainCipherContractReverse"));
		assertTrue(mutantQueries.get(187).getMutantMethodName().equals("ChainCipherContractTurnBack"));
		assertTrue(mutantQueries.get(188).getMutantMethodName().equals("ChainCipherSqueezeInvert"));
		assertTrue(mutantQueries.get(189).getMutantMethodName().equals("ChainCipherSqueezeReverse"));
		assertTrue(mutantQueries.get(190).getMutantMethodName().equals("ChainCipherSqueezeTurnBack"));
	}
}
