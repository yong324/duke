package duke.memoryaccess;

import duke.task.*;

import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Storage {

    private String filePath;
    private String fileName;
    private String fullFilePath;

    public Storage (String _filepath,String _fileName) {
        filePath = _filepath;
        fileName = _fileName;
        fullFilePath = filePath + fileName;
        assert fullFilePath  != null;
    }

    /**
     * Saves list data from Duke to memory
     */
    public void writeFile(ArrayList<Task> tasks) {

        try {
            FileWriter fw = new FileWriter(fullFilePath);
            for (int i = 0 ; i < tasks.size() ; i ++) {
                fw.write(tasks.get(i).taskToSaveFile() + "\n"); //+1 to i here due to numbering
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("Can't write to file!!");
        }

    };

    /**
     * Reads data file from memory and converts it into an Array of Strings, then processes it into an array of Tasks
     * @return an Array of tasks that is loaded when Duke is initialized
     */
    public ArrayList<Task> readFile()   {
        ArrayList<String> tasksInString  = new ArrayList<String>();
        try {
            File f = new File(fullFilePath);
            Scanner s = new Scanner(f);
            while (s.hasNext()) {
                tasksInString.add(s.nextLine());
            };
        } catch (FileNotFoundException e) {
            File f = new File(filePath);
            f.mkdir();
        }
        ArrayList<Task> tasksInTask = processTaskFromStringToTaskClass(tasksInString);
        return tasksInTask;
    };

    /**
     * Takes in an input of an Array of Strings, and converts it into an Array of Tasks
     * @return an Array of Tasks
     */
    public ArrayList<Task> processTaskFromStringToTaskClass(ArrayList<String> tasksInStringClass) {
        ArrayList<Task> tasksInTaskClass = new ArrayList<Task>();
        for (int counter = 0 ; counter < tasksInStringClass.size(); counter++) {
            tasksInTaskClass.add(processStringTask(tasksInStringClass.get(counter)));
        }
        return tasksInTaskClass;
    }

    /**
     * Processes String from saved data file to a task
     * @return A task, processed for input string
     */
    public Task processStringTask(String task) {
        Character taskType = task.charAt(0);


        String[] taskParsed = task.split("\\|");
        String taskName = taskParsed[2];
        String isDone = taskParsed[1];

        if (taskType.equals('T')) {
            ToDos newTodos = new ToDos(taskName);
            if (isDone.equals("1")) {
                newTodos.setDone(true);
            }
            return newTodos;

        } else if (taskType.equals('D')) {
            String deadlineDate = taskParsed[3];

            Deadlines newDeadline = new Deadlines(taskName, deadlineDate);
            if (isDone.equals("1")) {
                newDeadline.setDone(true);
            }
            return newDeadline;
        } else if (taskType.equals('E')){
            String eventDate = taskParsed[3];

            Events newEvent = new Events(taskName, eventDate);
            if (isDone.equals("1")) {
                newEvent.setDone(true);
            }
            return newEvent;
        } else {
            String eventStartDate = taskParsed[3];
            String eventEndDate = taskParsed[4];

            Schedule newSchedule = new Schedule(taskName, eventStartDate, eventEndDate);
            if (isDone.equals("1")) {
                newSchedule.setDone(true);
            }
            return newSchedule;
        }
    }
}
