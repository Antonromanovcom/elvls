package com.antonromanov.arnote.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "id")
@EqualsAndHashCode
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Table(name = "salary", schema = "arnote", catalog = "postgres")
public class Salary {

	@Id
	@Column(name="id", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "salary_seq_gen")
	@SequenceGenerator(name = "salary_seq_gen", sequenceName ="arnote.salary_id_seq", allocationSize = 1)
	private long id;

	@Column(name = "fullslary", nullable = true)
	private Integer fullSlary;

	@Column(name = "residualsalary", nullable = true)
	private Integer residualSalary;

	@Column(name = "date", nullable = true)
	@Temporal(TemporalType.DATE)
	private Date salarydate;

	@Column
	private LocalDateTime salaryTimeStamp;

	@ManyToOne(cascade = CascadeType.ALL)
	private LocalUser user;

	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	private List<JsonTest> children;

	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	private EnumSet<Animal> allAnimals;

	public Salary(Integer fullSlary, Integer residualSalary) {
		this.fullSlary = fullSlary;
		this.residualSalary = residualSalary;
	}

}
