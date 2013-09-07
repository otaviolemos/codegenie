-- Approachs
select Approach, recall, precis,
       (select count(distinct(afdescription)) from vwresponses where enumber = 1) as 'Functions', Total as 'Queries',
       (SELECT count(*) from vwsuccess1) 'Responses found',
       (SELECT count(*) from vwsuccess1) * 100 / Total as '% Responses found'
from   vwslice1 WHERE Slice = 'General' union
select Approach, recall, precis,
       (select count(distinct(afdescription)) from vwresponses where enumber = 2) as 'Functions', Total as 'Queries',
       (SELECT count(*) from vwsuccess2) 'Responses found',
       (SELECT count(*) from vwsuccess2) * 100 / Total as '% Responses found'
from   vwslice2 WHERE Slice = 'General' union
select Approach, recall, precis,
       (select count(distinct(afdescription)) from vwresponses where enumber = 3) as 'Functions', Total as 'Queries',
       (SELECT count(*) from vwsuccess3) 'Responses found',
       (SELECT count(*) from vwsuccess3) * 100 / Total as '% Responses found'
from   vwslice3 WHERE Slice = 'General' union
select Approach, recall, precis,
       (select count(distinct(afdescription)) from vwresponses where enumber = 4) as 'Functions', Total as 'Queries',
       (SELECT count(*) from vwsuccess4) 'Responses found',
       (SELECT count(*) from vwsuccess4) * 100 / Total as '% Responses found'
from   vwslice4 WHERE Slice = 'General'

-- Diferentes respostas encontradas e Respostas encontradas
select eAutoDescription, count(afrTotalIntersections) as 'Diferentes respostas encontradas', sum(afrTotalIntersections) as 'Respostas encontradas' from vwresponses
where eNumber = 1
and afrTotalIntersections > 0
UNION
select eAutoDescription, count(afrTotalIntersections) as 'Diferentes respostas encontradas', sum(afrTotalIntersections) as 'Respostas encontradas' from vwresponses
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
select 'Improvement', '36' as 'total', count(distinct(nome)) as 'pessoas beneficiadas', (count(distinct(nome)) * 100 / 36) as '% pessoas beneficiadas' from vwresponses
where eNumber = 5
and afrTotalIntersections > 0
and concat(nome, afDescription, afrTotalIntersections) not in (select concat(nome, afDescription, afrTotalIntersections) from vwresponses where eNumber = 1 and afrTotalIntersections > 0)
UNION
select 'Improvement - Distinct functions', '36' as 'total', count(distinct(nome)) as 'pessoas beneficiadas', (count(distinct(nome)) * 100 / 36) as '% pessoas beneficiadas' from vwresponses
where eNumber = 5
and afrTotalIntersections > 0
and concat(nome, afDescription) not in (select concat(nome, afDescription) from vwresponses where eNumber = 1 and afrTotalIntersections > 0)
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
select eAutoDescription, (select count(distinct(afdescription)) from vwresponses where enumber = 1) as 'total', count(distinct(afDescription)) as 'functions found', (count(distinct(afDescription)) * 100 / 15) as '% functions found' from vwresponses
where eNumber = 1
and afrTotalIntersections > 0
UNION
select eAutoDescription, (select count(distinct(afdescription)) from vwresponses where enumber = 5) as 'total', count(distinct(afDescription)) as 'functions found', (count(distinct(afDescription)) * 100 / 15) as '% functions found' from vwresponses
where eNumber = 5
and afrTotalIntersections > 0
UNION
select 'Improvement', (select count(distinct(afdescription)) from vwresponses where enumber = 5) as 'total', count(distinct(afDescription)) as 'functions found', (count(distinct(afDescription)) * 100 / 15) as '% functions found' from vwresponses
where eNumber = 5
and afrTotalIntersections > 0
and concat(nome, afDescription, afrTotalIntersections) not in (select concat(nome, afDescription, afrTotalIntersections) from vwresponses where eNumber = 1 and afrTotalIntersections > 0)
UNION
select 'Improvement - Distinct functions', (select count(distinct(afdescription)) from vwresponses where enumber = 5) as 'total', count(distinct(afDescription)) as 'functions found', (count(distinct(afDescription)) * 100 / 15) as '% functions found' from vwresponses
where eNumber = 5
and afrTotalIntersections > 0
and concat(nome, afDescription) not in (select concat(nome, afDescription) from vwresponses where eNumber = 1 and afrTotalIntersections > 0)
UNION
select 'Only expansions', (select count(distinct(afdescription)) from vwresponses where enumber = 5) as 'total', count(distinct(afDescription)) as 'functions found', (count(distinct(afDescription)) * 100 / 15) as '% functions found' from vwresponses
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

-- Resultados

-- Listas
select t1.nome, t1.afrMethodName, t1.afrReturnType, t1.afrParams,
       COALESCE(t2.afrPrecis,0) as 'precis', COALESCE(t2.afrRecall,0) as 'recall'
from
(select * from vwresponses where enumber = 1) as t1 left outer join
(select * from vwresponses where enumber = 5 and afrTotalIntersections > 0) as t2
on (t1.nome = t2.nome and t1.afDescription = t2.afDescription)
order by 1, 2

-- Médias
select count(*) as total, count(t2.afrPrecis) as 'success', avg(t2.afrPrecis) as presic, avg(t2.afrRecall) as recall
from
(select * from vwresponses where enumber = 1) as t1 left outer join
(select * from vwresponses where enumber = 1 and afrTotalIntersections > 0) as t2
on (t1.nome = t2.nome and t1.afDescription = t2.afDescription)
UNION
select count(*) as total, count(t2.afrPrecis) as 'success', avg(t2.afrPrecis) as presic, avg(t2.afrRecall) as recall
from
(select * from vwresponses where enumber = 1) as t1 left outer join
(select * from vwresponses where enumber = 5 and afrTotalIntersections > 0) as t2
on (t1.nome = t2.nome and t1.afDescription = t2.afDescription)
UNION
select count(*) as total, count(t2.afrPrecis) as 'success', avg(t2.afrPrecis) as presic, avg(t2.afrRecall) as recall
from
(select * from vwresponses where enumber = 1) as t1 left outer join
(select * from vwresponses where enumber = 6 and afrTotalIntersections > 0) as t2
on (t1.nome = t2.nome and t1.afDescription = t2.afDescription)
UNION
select count(*) as total, count(t2.afrPrecis) as 'success', avg(t2.afrPrecis) as presic, avg(t2.afrRecall) as recall
from
(select * from vwresponses where enumber = 1) as t1 left outer join
(select * from vwresponses where enumber = 7 and afrTotalIntersections > 0) as t2
on (t1.nome = t2.nome and t1.afDescription = t2.afDescription)
