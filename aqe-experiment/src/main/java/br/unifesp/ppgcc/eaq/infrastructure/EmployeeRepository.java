package br.unifesp.ppgcc.eaq.infrastructure;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.unifesp.ppgcc.eaq.domain.Employee;

@Repository("employeeRepository")
public class EmployeeRepository extends BaseRepository<Employee> {

	public List<Employee> findTitularesAtivos(){
		Criterion titular = Restrictions.eq("name", "Nemo");
		return findByCriteria(titular);
	}

}
