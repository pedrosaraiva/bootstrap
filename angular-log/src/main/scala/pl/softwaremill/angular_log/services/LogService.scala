package pl.softwaremill.angular_log.services

import javax.ws.rs._
import pl.softwaremill.angular_log.entities.LogObject
import java.util.Date
import scala.collection.JavaConversions._
import org.joda.time.DateTime

@Path("/logs")
class LogService {

  @GET
  @Produces(Array("application/json"))
  def getLogs():java.util.List[LogObject] = {
    println("Get logs");
    val javaList: java.util.List[LogObject] = Entries.list
    javaList
  }

  @GET
  @Path("/logs-count")
  @Produces(Array("application/json"))
  def getLogsCount() = {
     new LongResponseWrapper(Entries.list.size)
  }

  @POST
  @Consumes(Array("application/json"))
  def addNew(entry: LogObject) = {

    val newEntry: LogObject = new LogObject(Entries.nextId(), entry.text, entry.date, entry.author)
    Entries.list = Entries.list.::(newEntry)
    println("added new " + newEntry.toString)
  }

  @DELETE
  @Path("{entryId}")
  def remove(@PathParam("entryId") entryId: Long) = {
    println("Removing entry with id " + entryId)
    val entryOpt: Option[LogObject] = Entries.list.find((p: LogObject) => {
      p.id == entryId
    })

    if (entryOpt.isDefined) {
      Entries.list = Entries.list diff List(entryOpt.get)
    }
  }
}

object Entries {
  var id:Long  = 10;

  def nextId(): Long = {
    id = id +1;
    id
  }

  var list: List[LogObject] = List(
    new LogObject(4, "Still hot, the latest tweet", new DateTime().toString("HH:mm dd/MM/yyyy"), "Teofilia Chrust"),
    new LogObject(3, "This is really long tweet. This is really long tweet. This is really long tweet. " +
      "This is really long tweet. This is really long tweet. This is really long tweet.",
      new DateTime().withDate(2012, 8, 29).toString("HH:mm dd/MM/yyyy"), "Remigiusz Niemowa"),
    new LogObject(2, "Nothing less, nothing more, just second tweet",
      new DateTime().withDate(2012, 6, 4).toString("HH:mm dd/MM/yyyy"), "Krystian Krostka"),
    new LogObject(1, "Hello World! First entry",
      new DateTime().withDate(2012, 5, 20).toString("HH:mm dd/MM/yyyy"), "Jan Kowalski")
  )



}