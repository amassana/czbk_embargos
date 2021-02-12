package es.commerzbank.ice.embargos.config;

import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.job.JobTransferToTax;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;

import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

@Configuration
public class JobConfig {
    
	@Autowired
	private GeneralParametersService generalParametersService;
	
    //<editor-fold desc="JOBS">

    @Bean
    public JobDetailFactoryBean job1() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(JobTransferToTax.class);
        jobDetailFactory.setName(JobTransferToTax.class.getSimpleName());
        jobDetailFactory.setDescription("Job de traspaso de embargos a impuestos");
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

    //</editor-fold>
    
    //<editor-fold desc="TRIGGERS">
    
    @Bean
    public CronTriggerFactoryBean trigger1(JobDetail job1) {
    	String cronExpression = null;
    	try {
			cronExpression = generalParametersService.loadStringParameter(EmbargosConstants.CRON_JOB_EMBARGOS_TO_TAX);
		} catch (ICEException e) {
			cronExpression = "0 0/5 * * * ?";
			//cronExpression = "0 0 3 * * ?";
		}
    	
        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
        trigger.setName(job1.getKey().getName() + "Trigger");
        trigger.setJobDetail(job1);
        trigger.setCronExpression(cronExpression);
        return trigger;           
    }

    //</editor-fold>

    /***********/

}
