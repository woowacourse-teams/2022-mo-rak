package com.morak.back.brandnew.domain;

import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.RandomCodeGenerator;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class Poll {

    @Embedded
    private Code code;

    private String title;

    private Boolean anonymous;

    private int allowedCount;

    @ManyToOne
    private Member host;

    private Boolean closed;

    @Embedded
    private TempDateTime closedAt;

    @Builder
    public Poll(String title, Boolean anonymous, int allowedCount, Member host, Boolean closed,
                TempDateTime closedAt) {
        validateClosedAt(closedAt);
        this.code = Code.generate(new RandomCodeGenerator());
        this.title = title;
        this.anonymous = anonymous;
        this.allowedCount = allowedCount;
        this.host = host;
        this.closed = closed;
        this.closedAt = closedAt;
    }

    private void validateClosedAt(TempDateTime closedAt) {
        if (closedAt.beforeNow()) {
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

    public boolean isHost(Member member) {
        return host.isSame(member);
    }
}
