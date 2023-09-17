package tg.kindhands_bot.kindhands.components;

import tg.kindhands_bot.kindhands.repositories.UserRepository;
import tg.kindhands_bot.kindhands.services.KindHandsBot;

//public class ActionOnTime {
//    private final UserRepository userRepository;
//    private final KindHandsBot bot;
//    private final ProbationPeriod probationPeriod;
//
//    public ActionOnTime(UserRepository userRepository, KindHandsBot bot, ProbationPeriod probationPeriod) {
//        this.userRepository = userRepository;
//        this.bot = bot;
//        this.probationPeriod = probationPeriod;
//    }
//    @Scheduled(cron = "0 00 10 * * *") // каждый день в 10 утра
//    public void sendDailyReportReminder() {
//        Collection<User> allAdopters = userRepository.findAllWithExistingChatId();
//        for (User adopter : allAdopters) {
//            if (adopter.getProbationPeriod().stream().allMatch(
//                    probationPeriod -> probationPeriod.getEnds.isBefore(
//                            LocalDate.now()
//                    )
//            )) {
//                allAdopters.remove(adopter);
//            }
//        }
//        allAdopters.forEach(user -> {
//            bot.sendMessage(new SendMessage("Доброе утро! Напоминаем, что до 21:00 необходимо отправить " +
//                    "отчёт(ы) по питомцу(ам). Спасибо!"));
//
//        });
//    }
//
//}
