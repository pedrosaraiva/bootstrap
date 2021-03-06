package pl.softwaremill.bootstrap.service.schedulers

import pl.softwaremill.bootstrap.service.templates.EmailContentWithSubject
import collection.mutable.ListBuffer

class DummyEmailSendingService extends EmailSendingService with EmailScheduler {

  private val emailsToSend: ListBuffer[EmailToSend] = ListBuffer()

  def run() {
    var tempList: ListBuffer[EmailToSend] = null
    this.synchronized {
      tempList = ListBuffer(emailsToSend: _*)
      emailsToSend.clear()
    }
    logger.info("I should be sending emails now but I am dummy :)")
    for (email <- tempList) {
      logger.info("Dummy send email: " + email.toString)
    }

  }

  def scheduleEmail(address: String, emailData: EmailContentWithSubject) {
    this.synchronized {
      emailsToSend += EmailToSend(address, emailData.subject, emailData.content)
    }
    logger.debug("Email to " + address + " scheduled")
  }

  case class EmailToSend(address: String, subject: String, content: String)
}
