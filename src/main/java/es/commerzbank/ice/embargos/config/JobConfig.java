package es.commerzbank.ice.embargos.config;

import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.event.*;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

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

    @Bean
    public JobDetailFactoryBean jobDetailImportacionApuntesContables() {
      JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
      jobDetailFactory.setJobClass(JobImportacionApuntesContables.class);
      jobDetailFactory.setName(JobImportacionApuntesContables.class.getSimpleName());
      jobDetailFactory.setDescription("Job de importación de apuntes contables");
      jobDetailFactory.setDurability(true);
      return jobDetailFactory;
    }
    
    @Bean
    public CronTriggerFactoryBean triggerJobImportacionApuntesContables(JobDetail jobDetailImportacionApuntesContables) {
      String cronExpression = "0/10 * * * * ?";

      CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
      trigger.setName(jobDetailImportacionApuntesContables.getKey().getName() + "Trigger");
      trigger.setJobDetail(jobDetailImportacionApuntesContables);
      trigger.setCronExpression(cronExpression);
      return trigger;
    }
    
    @Bean
    public JobDetailFactoryBean job2() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(JobTaskPendingDate.class);
        jobDetailFactory.setName(JobTaskPendingDate.class.getSimpleName());
        jobDetailFactory.setDescription("Job de revisión de proximidad de fechas de tareas");
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }
    
    @Bean
    public CronTriggerFactoryBean trigger2(JobDetail job2) {
    	String cronExpression = null;
    	try {
			cronExpression = generalParametersService.loadStringParameter(EmbargosConstants.CRON_JOB_EMBARGOS_PENDING_TASK_DATE);
		} catch (ICEException e) {
			cronExpression = "0 0 0/1 ? * * *";
			//cronExpression = "0 0 3 * * ?";
		}
    	
        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
        trigger.setName(job2.getKey().getName() + "Trigger");
        trigger.setJobDetail(job2);
        trigger.setCronExpression(cronExpression);
        return trigger;           
    }

    @Bean
    public JobDetailFactoryBean jobNorma63FinalFileFactoryBean() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(JobNorma63FinalFile.class);
        jobDetailFactory.setName(JobNorma63FinalFile.class.getSimpleName());
        jobDetailFactory.setDescription("Job de generación del fichero de fin de ciclo de norma 63");
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

    @Bean
    public CronTriggerFactoryBean triggerNorma63FinalFile(JobDetail jobNorma63FinalFileFactoryBean) {
        String cronExpression = "0 */1 * ? * * *";
        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
        trigger.setName(jobNorma63FinalFileFactoryBean.getKey().getName() + "Trigger");
        trigger.setJobDetail(jobNorma63FinalFileFactoryBean);
        trigger.setCronExpression(cronExpression);
        return trigger;
    }

    @Bean
    public JobDetailFactoryBean jobImportCGPJFactoryBean() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(JobImportCGPJ.class);
        jobDetailFactory.setName(JobImportCGPJ.class.getSimpleName());
        jobDetailFactory.setDescription("Job de importación de solicitudes del cgpj");
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

    @Bean
    public CronTriggerFactoryBean triggerImportCGPJ(JobDetail jobImportCGPJFactoryBean) {
        String cronExpression = "7 */1 * ? * * *";
        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
        trigger.setName(jobImportCGPJFactoryBean.getKey().getName() + "Trigger");
        trigger.setJobDetail(jobImportCGPJFactoryBean);
        trigger.setCronExpression(cronExpression);
        return trigger;
    }

    @Bean
    public JobDetailFactoryBean jobPendingCGPJFactoryBean() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(JobCGPJPending.class);
        jobDetailFactory.setName(JobCGPJPending.class.getSimpleName());
        jobDetailFactory.setDescription("Job de email pendientes cgpj");
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

    @Bean
    public CronTriggerFactoryBean triggerPendientesCGPJ(JobDetail jobPendingCGPJFactoryBean) {
        String cronExpression = "0 57 * ? * * *";
        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
        trigger.setName(jobPendingCGPJFactoryBean.getKey().getName() + "Trigger");
        trigger.setJobDetail(jobPendingCGPJFactoryBean);
        trigger.setCronExpression(cronExpression);
        return trigger;
    }

    @Bean
    public JobDetailFactoryBean jobImportCGPJFestiveFactoryBean() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(JobImportCGPJFestive.class);
        jobDetailFactory.setName(JobImportCGPJ.class.getSimpleName());
        jobDetailFactory.setDescription("Job de importacion del cgpj festivos");
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

    @Bean
    public CronTriggerFactoryBean triggerJobCGPJFestiveImport(JobDetail jobImportCGPJFestiveFactoryBean) {
        String cronExpression = "0 */10 * ? * * *";
        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
        trigger.setName(jobImportCGPJFestiveFactoryBean.getKey().getName() + "Trigger");
        trigger.setJobDetail(jobImportCGPJFestiveFactoryBean);
        trigger.setCronExpression(cronExpression);
        return trigger;
    }
}
