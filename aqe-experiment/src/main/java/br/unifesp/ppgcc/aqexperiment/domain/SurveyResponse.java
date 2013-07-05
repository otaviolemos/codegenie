package br.unifesp.ppgcc.aqexperiment.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jxl.Sheet;


@Entity
@Table(name = "survey_response")
public class SurveyResponse {

	@Id
	@GeneratedValue
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date executionTimestamp;

	private String copiaNome;
	private String origem;
	private String conviteEmail;
	private String questoes;
	private String timestamp;
	private String nivelJava;
	private String nivelIngles;
	private String perfil;
	private String lixo;
	private String nome;
	private String email;
	private String leIngles;
	private String falaIngles;
	private String escreveIngles;
	private String compreendeIngles;
	private String vinculo;
	private String atuacao;
	private String return1;
	private String methodName1;
	private String params1;
	private String return2;
	private String methodName2;
	private String params2;
	private String return3;
	private String methodName3;
	private String params3;
	private String return4;
	private String methodName4;
	private String params4;
	private String return5;
	private String methodName5;
	private String params5;
	private String return6;
	private String methodName6;
	private String params6;
	private String return7;
	private String methodName7;
	private String params7;
	private String return8;
	private String methodName8;
	private String params8;
	private String return9;
	private String methodName9;
	private String params9;
	private String return10;
	private String methodName10;
	private String params10;
	private String return11;
	private String methodName11;
	private String params11;
	private String return12;
	private String methodName12;
	private String params12;
	private String return13;
	private String methodName13;
	private String params13;
	private String return14;
	private String methodName14;
	private String params14;
	private String return15;
	private String methodName15;
	private String params15;
	private String return16;
	private String methodName16;
	private String params16;
	private String return17;
	private String methodName17;
	private String params17;
	private String return18;
	private String methodName18;
	private String params18;
	private String return19;
	private String methodName19;
	private String params19;
	private String return20;
	private String methodName20;
	private String params20;
	private String return21;
	private String methodName21;
	private String params21;

	public SurveyResponse(){
	}
	
	public SurveyResponse(Sheet sheet, int line) {
		this.copiaNome = sheet.getCell(0, line).getContents();
		this.origem = sheet.getCell(1, line).getContents();
		this.conviteEmail = sheet.getCell(2, line).getContents();
		this.questoes = sheet.getCell(3, line).getContents();
		this.timestamp = sheet.getCell(4, line).getContents();
		this.nivelJava = sheet.getCell(5, line).getContents();
		this.nivelIngles = sheet.getCell(6, line).getContents();
		this.perfil = sheet.getCell(7, line).getContents();
		this.lixo = sheet.getCell(8, line).getContents();
		this.nome = sheet.getCell(9, line).getContents();
		this.email = sheet.getCell(10, line).getContents();
		this.leIngles = sheet.getCell(11, line).getContents();
		this.falaIngles = sheet.getCell(12, line).getContents();
		this.escreveIngles = sheet.getCell(13, line).getContents();
		this.compreendeIngles = sheet.getCell(14, line).getContents();
		this.vinculo = sheet.getCell(15, line).getContents();
		this.atuacao = sheet.getCell(16, line).getContents();
		this.return1 = sheet.getCell(17, line).getContents();
		this.methodName1 = sheet.getCell(18, line).getContents();
		this.params1 = sheet.getCell(19, line).getContents();
		this.return2 = sheet.getCell(20, line).getContents();
		this.methodName2 = sheet.getCell(21, line).getContents();
		this.params2 = sheet.getCell(22, line).getContents();
		this.return3 = sheet.getCell(23, line).getContents();
		this.methodName3 = sheet.getCell(24, line).getContents();
		this.params3 = sheet.getCell(25, line).getContents();
		this.return4 = sheet.getCell(26, line).getContents();
		this.methodName4 = sheet.getCell(27, line).getContents();
		this.params4 = sheet.getCell(28, line).getContents();
		this.return5 = sheet.getCell(29, line).getContents();
		this.methodName5 = sheet.getCell(30, line).getContents();
		this.params5 = sheet.getCell(31, line).getContents();
		this.return6 = sheet.getCell(32, line).getContents();
		this.methodName6 = sheet.getCell(33, line).getContents();
		this.params6 = sheet.getCell(34, line).getContents();
		this.return7 = sheet.getCell(35, line).getContents();
		this.methodName7 = sheet.getCell(36, line).getContents();
		this.params7 = sheet.getCell(37, line).getContents();
		this.return8 = sheet.getCell(38, line).getContents();
		this.methodName8 = sheet.getCell(39, line).getContents();
		this.params8 = sheet.getCell(40, line).getContents();
		this.return9 = sheet.getCell(41, line).getContents();
		this.methodName9 = sheet.getCell(42, line).getContents();
		this.params9 = sheet.getCell(43, line).getContents();
		this.return10 = sheet.getCell(44, line).getContents();
		this.methodName10 = sheet.getCell(45, line).getContents();
		this.params10 = sheet.getCell(46, line).getContents();
		this.return11 = sheet.getCell(47, line).getContents();
		this.methodName11 = sheet.getCell(48, line).getContents();
		this.params11 = sheet.getCell(49, line).getContents();
		this.return12 = sheet.getCell(50, line).getContents();
		this.methodName12 = sheet.getCell(51, line).getContents();
		this.params12 = sheet.getCell(52, line).getContents();
		this.return13 = sheet.getCell(53, line).getContents();
		this.methodName13 = sheet.getCell(54, line).getContents();
		this.params13 = sheet.getCell(55, line).getContents();
		this.return14 = sheet.getCell(56, line).getContents();
		this.methodName14 = sheet.getCell(57, line).getContents();
		this.params14 = sheet.getCell(58, line).getContents();
		this.return15 = sheet.getCell(59, line).getContents();
		this.methodName15 = sheet.getCell(60, line).getContents();
		this.params15 = sheet.getCell(61, line).getContents();
		this.return16 = sheet.getCell(62, line).getContents();
		this.methodName16 = sheet.getCell(63, line).getContents();
		this.params16 = sheet.getCell(64, line).getContents();
		this.return17 = sheet.getCell(65, line).getContents();
		this.methodName17 = sheet.getCell(66, line).getContents();
		this.params17 = sheet.getCell(67, line).getContents();
		this.return18 = sheet.getCell(68, line).getContents();
		this.methodName18 = sheet.getCell(69, line).getContents();
		this.params18 = sheet.getCell(70, line).getContents();
		this.return19 = sheet.getCell(71, line).getContents();
		this.methodName19 = sheet.getCell(72, line).getContents();
		this.params19 = sheet.getCell(73, line).getContents();
		this.return20 = sheet.getCell(74, line).getContents();
		this.methodName20 = sheet.getCell(75, line).getContents();
		this.params20 = sheet.getCell(76, line).getContents();
		this.return21 = sheet.getCell(77, line).getContents();
		this.methodName21 = sheet.getCell(78, line).getContents();
		this.params21 = sheet.getCell(79, line).getContents();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getExecutionTimestamp() {
		return executionTimestamp;
	}
	public void setExecutionTimestamp(Date executionTimestamp) {
		this.executionTimestamp = executionTimestamp;
	}
	public String getCopiaNome() {
		return copiaNome;
	}
	public void setCopiaNome(String copiaNome) {
		this.copiaNome = copiaNome;
	}
	public String getOrigem() {
		return origem;
	}
	public void setOrigem(String origem) {
		this.origem = origem;
	}
	public String getConviteEmail() {
		return conviteEmail;
	}
	public void setConviteEmail(String conviteEmail) {
		this.conviteEmail = conviteEmail;
	}
	public String getQuestoes() {
		return questoes;
	}
	public void setQuestoes(String questoes) {
		this.questoes = questoes;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getNivelJava() {
		return nivelJava;
	}
	public void setNivelJava(String nivelJava) {
		this.nivelJava = nivelJava;
	}
	public String getNivelIngles() {
		return nivelIngles;
	}
	public void setNivelIngles(String nivelIngles) {
		this.nivelIngles = nivelIngles;
	}
	public String getPerfil() {
		return perfil;
	}
	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}
	public String getLixo() {
		return lixo;
	}
	public void setLixo(String lixo) {
		this.lixo = lixo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLeIngles() {
		return leIngles;
	}
	public void setLeIngles(String leIngles) {
		this.leIngles = leIngles;
	}
	public String getFalaIngles() {
		return falaIngles;
	}
	public void setFalaIngles(String falaIngles) {
		this.falaIngles = falaIngles;
	}
	public String getEscreveIngles() {
		return escreveIngles;
	}
	public void setEscreveIngles(String escreveIngles) {
		this.escreveIngles = escreveIngles;
	}
	public String getCompreendeIngles() {
		return compreendeIngles;
	}
	public void setCompreendeIngles(String compreendeIngles) {
		this.compreendeIngles = compreendeIngles;
	}
	public String getVinculo() {
		return vinculo;
	}
	public void setVinculo(String vinculo) {
		this.vinculo = vinculo;
	}
	public String getAtuacao() {
		return atuacao;
	}
	public void setAtuacao(String atuacao) {
		this.atuacao = atuacao;
	}
	public String getReturn1() {
		return return1;
	}
	public void setReturn1(String return1) {
		this.return1 = return1;
	}
	public String getMethodName1() {
		return methodName1;
	}
	public void setMethodName1(String methodName1) {
		this.methodName1 = methodName1;
	}
	public String getParams1() {
		return params1;
	}
	public void setParams1(String params1) {
		this.params1 = params1;
	}
	public String getReturn2() {
		return return2;
	}
	public void setReturn2(String return2) {
		this.return2 = return2;
	}
	public String getMethodName2() {
		return methodName2;
	}
	public void setMethodName2(String methodName2) {
		this.methodName2 = methodName2;
	}
	public String getParams2() {
		return params2;
	}
	public void setParams2(String params2) {
		this.params2 = params2;
	}
	public String getReturn3() {
		return return3;
	}
	public void setReturn3(String return3) {
		this.return3 = return3;
	}
	public String getMethodName3() {
		return methodName3;
	}
	public void setMethodName3(String methodName3) {
		this.methodName3 = methodName3;
	}
	public String getParams3() {
		return params3;
	}
	public void setParams3(String params3) {
		this.params3 = params3;
	}
	public String getReturn4() {
		return return4;
	}
	public void setReturn4(String return4) {
		this.return4 = return4;
	}
	public String getMethodName4() {
		return methodName4;
	}
	public void setMethodName4(String methodName4) {
		this.methodName4 = methodName4;
	}
	public String getParams4() {
		return params4;
	}
	public void setParams4(String params4) {
		this.params4 = params4;
	}
	public String getReturn5() {
		return return5;
	}
	public void setReturn5(String return5) {
		this.return5 = return5;
	}
	public String getMethodName5() {
		return methodName5;
	}
	public void setMethodName5(String methodName5) {
		this.methodName5 = methodName5;
	}
	public String getParams5() {
		return params5;
	}
	public void setParams5(String params5) {
		this.params5 = params5;
	}
	public String getReturn6() {
		return return6;
	}
	public void setReturn6(String return6) {
		this.return6 = return6;
	}
	public String getMethodName6() {
		return methodName6;
	}
	public void setMethodName6(String methodName6) {
		this.methodName6 = methodName6;
	}
	public String getParams6() {
		return params6;
	}
	public void setParams6(String params6) {
		this.params6 = params6;
	}
	public String getReturn7() {
		return return7;
	}
	public void setReturn7(String return7) {
		this.return7 = return7;
	}
	public String getMethodName7() {
		return methodName7;
	}
	public void setMethodName7(String methodName7) {
		this.methodName7 = methodName7;
	}
	public String getParams7() {
		return params7;
	}
	public void setParams7(String params7) {
		this.params7 = params7;
	}
	public String getReturn8() {
		return return8;
	}
	public void setReturn8(String return8) {
		this.return8 = return8;
	}
	public String getMethodName8() {
		return methodName8;
	}
	public void setMethodName8(String methodName8) {
		this.methodName8 = methodName8;
	}
	public String getParams8() {
		return params8;
	}
	public void setParams8(String params8) {
		this.params8 = params8;
	}
	public String getReturn9() {
		return return9;
	}
	public void setReturn9(String return9) {
		this.return9 = return9;
	}
	public String getMethodName9() {
		return methodName9;
	}
	public void setMethodName9(String methodName9) {
		this.methodName9 = methodName9;
	}
	public String getParams9() {
		return params9;
	}
	public void setParams9(String params9) {
		this.params9 = params9;
	}
	public String getReturn10() {
		return return10;
	}
	public void setReturn10(String return10) {
		this.return10 = return10;
	}
	public String getMethodName10() {
		return methodName10;
	}
	public void setMethodName10(String methodName10) {
		this.methodName10 = methodName10;
	}
	public String getParams10() {
		return params10;
	}
	public void setParams10(String params10) {
		this.params10 = params10;
	}
	public String getReturn11() {
		return return11;
	}
	public void setReturn11(String return11) {
		this.return11 = return11;
	}
	public String getMethodName11() {
		return methodName11;
	}
	public void setMethodName11(String methodName11) {
		this.methodName11 = methodName11;
	}
	public String getParams11() {
		return params11;
	}
	public void setParams11(String params11) {
		this.params11 = params11;
	}
	public String getReturn12() {
		return return12;
	}
	public void setReturn12(String return12) {
		this.return12 = return12;
	}
	public String getMethodName12() {
		return methodName12;
	}
	public void setMethodName12(String methodName12) {
		this.methodName12 = methodName12;
	}
	public String getParams12() {
		return params12;
	}
	public void setParams12(String params12) {
		this.params12 = params12;
	}
	public String getReturn13() {
		return return13;
	}
	public void setReturn13(String return13) {
		this.return13 = return13;
	}
	public String getMethodName13() {
		return methodName13;
	}
	public void setMethodName13(String methodName13) {
		this.methodName13 = methodName13;
	}
	public String getParams13() {
		return params13;
	}
	public void setParams13(String params13) {
		this.params13 = params13;
	}
	public String getReturn14() {
		return return14;
	}
	public void setReturn14(String return14) {
		this.return14 = return14;
	}
	public String getMethodName14() {
		return methodName14;
	}
	public void setMethodName14(String methodName14) {
		this.methodName14 = methodName14;
	}
	public String getParams14() {
		return params14;
	}
	public void setParams14(String params14) {
		this.params14 = params14;
	}
	public String getReturn15() {
		return return15;
	}
	public void setReturn15(String return15) {
		this.return15 = return15;
	}
	public String getMethodName15() {
		return methodName15;
	}
	public void setMethodName15(String methodName15) {
		this.methodName15 = methodName15;
	}
	public String getParams15() {
		return params15;
	}
	public void setParams15(String params15) {
		this.params15 = params15;
	}
	public String getReturn16() {
		return return16;
	}
	public void setReturn16(String return16) {
		this.return16 = return16;
	}
	public String getMethodName16() {
		return methodName16;
	}
	public void setMethodName16(String methodName16) {
		this.methodName16 = methodName16;
	}
	public String getParams16() {
		return params16;
	}
	public void setParams16(String params16) {
		this.params16 = params16;
	}
	public String getReturn17() {
		return return17;
	}
	public void setReturn17(String return17) {
		this.return17 = return17;
	}
	public String getMethodName17() {
		return methodName17;
	}
	public void setMethodName17(String methodName17) {
		this.methodName17 = methodName17;
	}
	public String getParams17() {
		return params17;
	}
	public void setParams17(String params17) {
		this.params17 = params17;
	}
	public String getReturn18() {
		return return18;
	}
	public void setReturn18(String return18) {
		this.return18 = return18;
	}
	public String getMethodName18() {
		return methodName18;
	}
	public void setMethodName18(String methodName18) {
		this.methodName18 = methodName18;
	}
	public String getParams18() {
		return params18;
	}
	public void setParams18(String params18) {
		this.params18 = params18;
	}
	public String getReturn19() {
		return return19;
	}
	public void setReturn19(String return19) {
		this.return19 = return19;
	}
	public String getMethodName19() {
		return methodName19;
	}
	public void setMethodName19(String methodName19) {
		this.methodName19 = methodName19;
	}
	public String getParams19() {
		return params19;
	}
	public void setParams19(String params19) {
		this.params19 = params19;
	}
	public String getReturn20() {
		return return20;
	}
	public void setReturn20(String return20) {
		this.return20 = return20;
	}
	public String getMethodName20() {
		return methodName20;
	}
	public void setMethodName20(String methodName20) {
		this.methodName20 = methodName20;
	}
	public String getParams20() {
		return params20;
	}
	public void setParams20(String params20) {
		this.params20 = params20;
	}
	public String getReturn21() {
		return return21;
	}
	public void setReturn21(String return21) {
		this.return21 = return21;
	}
	public String getMethodName21() {
		return methodName21;
	}
	public void setMethodName21(String methodName21) {
		this.methodName21 = methodName21;
	}
	public String getParams21() {
		return params21;
	}
	public void setParams21(String params21) {
		this.params21 = params21;
	}
}
