package pl.piekoszek.ing.endpoints.atm;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeMap;

@Component
class AtmService {
    List<ATM> calculate(@RequestBody Task[] tasks) {
        var regionToTask = new TreeMap<Integer, List<Task>>();
        for (Task task : tasks) {
            regionToTask.putIfAbsent(task.region(), new ArrayList<>());
            regionToTask.get(task.region()).add(task);
        }
        var result = new LinkedHashSet<ATM>();
        regionToTask.forEach((region, ts) -> {

            var failureRestart = new ArrayList<ATM>();
            var priority = new ArrayList<ATM>();
            var signalLow = new ArrayList<ATM>();
            var standard = new ArrayList<ATM>();

            for (Task task : ts) {
                var list = switch (task.requestType()) {
                    case STANDARD -> standard;
                    case PRIORITY -> priority;
                    case SIGNAL_LOW -> signalLow;
                    case FAILURE_RESTART -> failureRestart;
                };
                list.add(new ATM(task.region(), task.atmId()));
            }
            result.addAll(failureRestart);
            result.addAll(priority);
            result.addAll(signalLow);
            result.addAll(standard);
        });
        return result.stream().toList();
    }
}
