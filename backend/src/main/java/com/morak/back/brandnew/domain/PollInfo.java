package com.morak.back.brandnew.domain;

import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.RandomCodeGenerator;
import com.morak.back.poll.domain.PollStatus;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class PollInfo {

    @Embedded
    private Code code;

    private String title;

    private Boolean anonymous;

    private int allowedCount;

    private String teamCode;

    private Long hostId;

    @Enumerated(EnumType.STRING)
    private PollStatus status;

    @Embedded
    private TempDateTime closedAt;

    @Builder
    public PollInfo(String title, Boolean anonymous, int allowedCount, String teamCode, Long hostId, PollStatus status,
                    TempDateTime closedAt) {
        validateClosedAt(closedAt);
        this.code = Code.generate(new RandomCodeGenerator());
        this.title = title;
        this.anonymous = anonymous;
        this.allowedCount = allowedCount;
        this.teamCode = teamCode;
        this.hostId = hostId;
        this.status = status;
        this.closedAt = closedAt;
    }

    private void validateClosedAt(TempDateTime closedAt) {
        if (closedAt.beforeNow()) {
            throw new IllegalArgumentException();
        }
    }

    public String getCode() {
        return this.code.getCode();
    }

    public boolean isGreaterThan(int count) {
        return allowedCount > count;
    }

    public void close(Long memberId) {
        validateHost(memberId);
        validateClose();
        status.close();
    }

    private void validateClose() {
        if (status.isClosed()) {
            throw new IllegalArgumentException("이미 끝남");
        }
    }

    private void validateHost(Long memberId) {
        if (!hostId.equals(memberId)) {
            throw new IllegalArgumentException("호스트 아님");
        }
    }

    public boolean isHost(Long memberId) {
        return hostId.equals(memberId);
    }

    public boolean isClosed() {
        return status.isClosed();
    }
}
