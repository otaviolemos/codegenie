-- Funções diferentes encontradas e Funções encontradas
select eAutoDescription, count(afrTotalIntersections) as 'different functions found', sum(afrTotalIntersections) as 'functions found' from vwresponses
where eNumber = 1
and afrTotalIntersections > 0
UNION
select eAutoDescription, count(afrTotalIntersections) as 'different functions found', sum(afrTotalIntersections) as 'functions found' from vwresponses
where eNumber = 5
and afrTotalIntersections > 0

-- Pessoas --

-- % Pessoas beneficiadas
select eAutoDescription, '36' as 'total', count(distinct(nome)) as 'pessoas beneficiadas', (count(distinct(nome)) * 100 / 36) as '% pessoas beneficiadas' from vwresponses
where eNumber = 1
and afrTotalIntersections > 0
UNION
select eAutoDescription, '36' as 'total', count(distinct(nome)) as 'pessoas beneficiadas', (count(distinct(nome)) * 100 / 36) as '% pessoas beneficiadas' from vwresponses
where eNumber = 5
and afrTotalIntersections > 0
UNION
select 'Only expansions', '36' as 'total', count(distinct(nome)) as 'pessoas beneficiadas', (count(distinct(nome)) * 100 / 36) as '% pessoas beneficiadas' from vwresponses
where eNumber = 5
and afrTotalIntersections > 0
and nome not in (select nome from vwresponses where eNumber = 1 and afrTotalIntersections > 0)

-- Pessoas beneficiadas só com a expansão
select eAutoDescription, nome as 'pessoas beneficiadas', count(afDescription) as 'functions' from vwresponses
where eNumber = 5
and afrTotalIntersections > 0
and nome not in (select nome from vwresponses where eNumber = 1 and afrTotalIntersections > 0)
group by  eAutoDescription, nome

-- Funções --

-- % Funções encontradas
select eAutoDescription, '15' as 'total', count(distinct(afDescription)) as 'functions found', (count(distinct(afDescription)) * 100 / 15) as '% functions found' from vwresponses
where eNumber = 1
and afrTotalIntersections > 0
UNION
select eAutoDescription, '15' as 'total', count(distinct(afDescription)) as 'functions found', (count(distinct(afDescription)) * 100 / 15) as '% functions found' from vwresponses
where eNumber = 5
and afrTotalIntersections > 0
UNION
select 'Only expansions', '15' as 'total', count(distinct(afDescription)) as 'functions found', (count(distinct(afDescription)) * 100 / 15) as '% functions found' from vwresponses
where eNumber = 5
and afrTotalIntersections > 0
and afDescription not in (select afDescription from vwresponses where eNumber = 1 and afrTotalIntersections > 0)

-- Funções encontradas só com a expansão
select eAutoDescription, afDescription as 'functions found', count(nome) as 'people' from vwresponses
where eNumber = 5
and afrTotalIntersections > 0
and afDescription not in (select afDescription from vwresponses where eNumber = 1 and afrTotalIntersections > 0)
group by  eAutoDescription, afDescription

-- Respostas --

-- Respostas encontradas só com a expansão
select eAutoDescription, nome, afDescription, afrTotalIntersections, afrTotalRelevants, afrTotalResults
from vwresponses
where eNumber = 5
and afrTotalIntersections > 0
and concat(nome, afDescription) not in (select concat(nome, afDescription) from vwresponses where eNumber = 1 and afrTotalIntersections > 0)
order by 3,2

-- Subgrupos --

-- Recall para grupos de pessoas
select t1.Slice as 'Slice - Recall',
       t1.recall as '1. Without expansions',
       t2.recall as '2. WordNet',
       t3.recall as '3. CodeVocabulary',
       t4.recall as '4. Type',
       t5.recall as '5. WordNet, CodeVocabulary, Type'
FROM
(select * from vwSlice1) as t1,
(select * from vwSlice2) as t2,
(select * from vwSlice3) as t3,
(select * from vwSlice4) as t4,
(select * from vwSlice5) as t5
where t1.Slice = t2.Slice
and   t1.Slice = t3.Slice
and   t1.Slice = t4.Slice
and   t1.Slice = t5.Slice

-- Precision para grupos de pessoas
select t1.Slice as 'Slice - Precision',
       t1.precis as '1. Without expansions',
       t2.precis as '2. WordNet',
       t3.precis as '3. CodeVocabulary',
       t4.precis as '4. Type',
       t5.precis as '5. WordNet, CodeVocabulary, Type'
FROM
(select * from vwSlice1) as t1,
(select * from vwSlice2) as t2,
(select * from vwSlice3) as t3,
(select * from vwSlice4) as t4,
(select * from vwSlice5) as t5
where t1.Slice = t2.Slice
and   t1.Slice = t3.Slice
and   t1.Slice = t4.Slice
and   t1.Slice = t5.Slice

-- Recall para grupos de pessoas sobre as respostas encontradas
select t1.Slice as 'Slice - Recall',
       t1.recall as '1. Without expansions',
       t2.recall as '2. WordNet',
       t3.recall as '3. CodeVocabulary',
       t4.recall as '4. Type',
       t5.recall as '5. WordNet, CodeVocabulary, Type'
FROM
(select * from vwSuccessSlice1) as t1,
(select * from vwSuccessSlice2) as t2,
(select * from vwSuccessSlice3) as t3,
(select * from vwSuccessSlice4) as t4,
(select * from vwSuccessSlice5) as t5
where t1.Slice = t2.Slice
and   t1.Slice = t3.Slice
and   t1.Slice = t4.Slice
and   t1.Slice = t5.Slice

-- Precision para grupos de pessoas sobre as respostas encontradas
select t1.Slice as 'Slice - Precision',
       t1.precis as '1. Without expansions',
       t2.precis as '2. WordNet',
       t3.precis as '3. CodeVocabulary',
       t4.precis as '4. Type',
       t5.precis as '5. WordNet, CodeVocabulary, Type'
FROM
(select * from vwSuccessSlice1) as t1,
(select * from vwSuccessSlice2) as t2,
(select * from vwSuccessSlice3) as t3,
(select * from vwSuccessSlice4) as t4,
(select * from vwSuccessSlice5) as t5
where t1.Slice = t2.Slice
and   t1.Slice = t3.Slice
and   t1.Slice = t4.Slice
and   t1.Slice = t5.Slice

-- % Encontradas para grupos de pessoas sobre as respostas encontradas
select t1.Slice as 'Slice - % Found',
       concat(t1.successPercent,'%') as '1. Without expansions',
       concat(t2.successPercent,'%') as '2. WordNet',
       concat(t3.successPercent,'%') as '3. CodeVocabulary',
       concat(t4.successPercent,'%') as '4. Type',
       concat(t5.successPercent,'%') as '5. WordNet, CodeVocabulary, Type'
FROM
(select * from vwSuccessSlice1) as t1,
(select * from vwSuccessSlice2) as t2,
(select * from vwSuccessSlice3) as t3,
(select * from vwSuccessSlice4) as t4,
(select * from vwSuccessSlice5) as t5
where t1.Slice = t2.Slice
and   t1.Slice = t3.Slice
and   t1.Slice = t4.Slice
and   t1.Slice = t5.Slice
