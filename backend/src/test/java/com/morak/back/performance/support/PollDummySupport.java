package com.morak.back.performance.support;

import com.morak.back.performance.dao.PollDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("performance")
public class PollDummySupport {

    @Autowired
    private PollDao pollDao;

//    public void 투표_더미데이터를_추가한다(int teamSize, int pollSizePerTeam) {
//        List<Poll> polls = makeDummyPolls(teamSize, pollSizePerTeam);
//        pollDao.batchInsertPolls(polls);
//    }

//    public void 투표_선택항목_더미데이터를_추가한다(int pollSize, int pollItemSizePerPoll) {
//        List<PollItem> pollItems = makeDummyPollItems(pollSize, pollItemSizePerPoll);
//        pollDao.batchInsertPollItems(pollItems);
//    }

//    public void 투표_선택결과_더미데이터를_추가한다(int pollItemSize) {
//        List<PollResult> pollResults1 = makeDummyPollResult(MEMBER_ID1, pollItemSize);
//        List<PollResult> pollResults2 = makeDummyPollResult(MEMBER_ID2, pollItemSize);
//        pollResults1.addAll(pollResults2);
//        pollDao.batchInsertPollResults(pollResults1);
//    }

//    public List<Poll> makeDummyPolls(int teamSize, int pollSizePerTeam) {
//        return Stream.iterate(1L, i -> i <= teamSize, i -> i + 1)
//                .flatMap(teamIndex -> IntStream.rangeClosed(1, pollSizePerTeam)
//                        .mapToObj(pollIndex -> Poll.builder()
//                                (Team.builder().id(teamIndex).build())
//                                .host(MEMBER_ID1)
//                                .title("더미 투표" + pollIndex)
//                                .allowedPollCount(3)
//                                .isAnonymous(false)
//                                .status(OPEN)
//                                .closedAt(LocalDateTime.now().plusDays(1L))
//                                .code(Code.generate(new RandomCodeGenerator()))
//                                .build())
//                        .collect(Collectors.toList())
//                        .stream())
//                .collect(Collectors.toList());
//    }

//    public List<PollItem> makeDummyPollItems(int pollSize, int pollItemSizePerPoll) {
//        return Stream.iterate(1L, i -> i <= pollSize, i -> i + 1)
//                .flatMap(pollIndex -> IntStream.rangeClosed(1, pollItemSizePerPoll)
//                        .mapToObj(pollItemIndex -> PollItem.builder()
//                                .poll(Poll.builder().id(pollIndex).build())
//                                .subject("더미 투표 선택 항목" + pollItemIndex)
//                                .build())
//                        .collect(Collectors.toList())
//                        .stream())
//                .collect(Collectors.toList());
//    }

//    public List<PollResult> makeDummyPollResult(Member member, int pollItemSize) {
//        return LongStream.rangeClosed(1L, pollItemSize)
//                .mapToObj(pollItemIndex -> PollResult.builder()
//                        .pollItem(PollItem.builder().id(pollItemIndex).poll(Poll.builder().build()).build())
//                        .member(member)
//                        .description("더미 투표 선택 결과")
//                        .build())
//                .collect(Collectors.toList());
//    }
}
