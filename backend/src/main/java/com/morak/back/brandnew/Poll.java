package com.morak.back.brandnew;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Poll {

    private final String title;
    private final boolean anonymous;
    private final int allowedCount;
    private final Member host;
    private boolean closed;
    private final MorakDateTime closedAt;

    @Builder
    public Poll(String title, boolean anonymous, int allowedCount, Member host, boolean closed,
                MorakDateTime closedAt) {
        validateClosedAt(closedAt);
        this.title = title;
        this.anonymous = anonymous;
        this.allowedCount = allowedCount;
        this.host = host;
        this.closed = closed;
        this.closedAt = closedAt;
    }

    private void validateClosedAt(MorakDateTime closedAt) {
        if (closedAt.isBeforeNow()) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isGreaterThan(int count) {
        return allowedCount < count;
    }

    public void close(Member member) {
        validateHost(member);
        validateClose();
        closed = true;
    }

    private void validateClose() {
        if (closed) {
            throw new IllegalArgumentException("이미 끝남");
        }
    }

    private void validateHost(Member member) {
        if (!host.isSame(member)) {
            throw new IllegalArgumentException("호스트 아님");
        }
    }
}
