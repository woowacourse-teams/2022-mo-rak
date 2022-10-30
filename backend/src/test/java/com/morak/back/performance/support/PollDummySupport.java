package com.morak.back.performance.support;

import com.morak.back.performance.dao.PollDao;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("performance")
public class PollDummySupport {

    @Autowired
    private PollDao pollDao;

    public void 투표_더미데이터를_추가한다(int teamSize, int pollSizePerTeam) {
        pollDao.batchInsertPolls(makeDummyPolls(1L, teamSize, pollSizePerTeam));
    }

    public void 투표_선택항목_더미데이터를_추가한다(int pollSize, int pollItemSizePerPoll) {
        pollDao.batchInsertPollItems(makeDummyPollItems(pollSize, pollItemSizePerPoll));
    }

    public void 투표_선택결과_더미데이터를_추가한다(int pollItemSize) {
        List<Entry<Long, Long>> pollResults1 = makeDummyPollResult(1L, pollItemSize);
        pollResults1.addAll(makeDummyPollResult(2L, pollItemSize));
        pollDao.batchInsertPollResults(pollResults1);
    }

    private List<Entry<String, Long>> makeDummyPolls(long hostId, int teamSize, int pollSizePerTeam) {
        return Stream.iterate(1L, i -> i <= teamSize, i -> i + 1)
                .flatMap(teamIndex -> IntStream.rangeClosed(1, pollSizePerTeam)
                        .mapToObj(pollIndex -> Map.entry(String.format("%08d", teamIndex), hostId))
                        .collect(Collectors.toList())
                        .stream())
                .collect(Collectors.toList());
    }

    private List<Long> makeDummyPollItems(int pollSize, int pollItemSizePerPoll) {
        return Stream.iterate(1L, i -> i <= pollSize, i -> i + 1)
                .flatMap(pollIndex -> IntStream.rangeClosed(1, pollItemSizePerPoll)
                        .mapToObj(pollItemIndex -> pollIndex)
                        .collect(Collectors.toList())
                        .stream())
                .collect(Collectors.toList());
    }

    public List<Entry<Long, Long>> makeDummyPollResult(Long memberId, int pollItemSize) {
        return LongStream.rangeClosed(1L, pollItemSize)
                .mapToObj(pollItemIndex -> Map.entry(pollItemIndex, memberId))
                .collect(Collectors.toList());
    }
}
