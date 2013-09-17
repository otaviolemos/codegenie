<html>
<body>
	<h1>It works!</h1>
	<form name="search" action="search" method="post">
		<table border="0">
			<tr>
				<td>Return</td>
				<td>Method name</td>
				<td>Params</td>
				<td>Expanders</td>
				<td>&nbsp</td>
			</tr>
			<tr>
				<td><input type="text" name="return"></td>
				<td><input type="text" name="methodName"></td>
				<td><input type="text" name="params"></td>
				<td>
					<input type="checkbox" name="w" value="WordNet">WordNet
					<input type="checkbox" name="c" value="CodeVocabulary">Code-Vocabulary
					<input type="checkbox" name="t" value="Type">Type
				</td>
				<td><input type="submit" value="Search"></td>
			</tr>
		</table>
		<br>
		${result}

	</form>
</body>
</html>