package com.morak.back.poll.domain;

import com.morak.back.core.domain.Code;
import com.morak.back.core.domain.CodeGenerator;
import com.morak.back.core.exception.CustomErrorCode;
import com.morak.back.poll.exception.PollAuthorizationException;
import com.morak.back.poll.exception.PollDomainLogicException;
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

    @Embedded
    private AllowedCount allowedCount;

    private Long teamId;

    private Long hostId;

    @Enumerated(EnumType.STRING)
    private PollStatus status;

    @Embedded
    private SystemDateTime closedAt;

    @Builder
    public PollInfo(CodeGenerator codeGenerator, String title, Boolean anonymous, int allowedCount, Long teamId, Long hostId, PollStatus status,
                    SystemDateTime closedAt) {
        validateClosedAt(closedAt);
        this.code = Code.generate(codeGenerator);
        this.title = title;
        this.anonymous = anonymous;
        this.allowedCount = new AllowedCount(allowedCount);
        this.teamId = teamId;
        this.hostId = hostId;
        this.status = status;
        this.closedAt = closedAt;
    }

    private void validateClosedAt(SystemDateTime closedAt) {
        if (closedAt.beforeNow()) {
            throw new PollDomainLogicException(
                    CustomErrorCode.POLL_CLOSED_AT_OUT_OF_RANGE_ERROR,
                    closedAt + "은 현재보다 미래가 아닙니다."
            );
        }
    }

    public String getCode() {
        return this.code.getCode();
    }

    public boolean isAllowedCount(int itemCount) {
        return itemCount >= 1 && isAllowedCountLessThan(itemCount);
    }

    public void close(Long memberId) {
        validateHost(memberId);
        status = status.close();
    }

    private void validateHost(Long memberId) {
        if (!hostId.equals(memberId)) {
            throw new PollAuthorizationException(
                    CustomErrorCode.POLL_HOST_MISMATCHED_ERROR,
                    memberId + "번 멤버는 " + getCode() + " 코드 투표의 호스트가 아닙니다."
            );
        }
    }

    public boolean isHost(Long memberId) {
        return hostId.equals(memberId);
    }

    public boolean isClosed() {
        return status.isClosed();
    }

    public boolean isAllowedCountGraterThan(int itemCount) {
        return this.allowedCount.isGraterThan(itemCount);
    }

    public boolean isAllowedCountLessThan(int itemCount) {
        return this.allowedCount.isLessThan(itemCount);
    }
}
