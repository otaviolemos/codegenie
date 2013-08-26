package br.unifesp.ppgcc.aqexperiment.infrastructure;

import org.springframework.stereotype.Repository;

import br.unifesp.ppgcc.aqexperiment.domain.Execution;

@Repository("executionRepository")
public class ExecutionRepository extends BaseRepository<Execution> {

}
