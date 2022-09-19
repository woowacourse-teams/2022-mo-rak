package com.morak.back.appointment.domain.appointment.timeconditions;

import static com.morak.back.core.exception.CustomErrorCode.AVAILABLETIME_DATE_OUT_OF_RANGE_ERROR;
import static com.morak.back.core.exception.CustomErrorCode.AVAILABLETIME_DURATION_NOT_MINUTES_UNIT_ERROR;
import static com.morak.back.core.exception.CustomErrorCode.AVAILABLETIME_NOT_DIVIDED_BY_MINUTES_UNIT_ERROR;
import static com.morak.back.core.exception.CustomErrorCode.AVAILABLETIME_TIME_OUT_OF_RANGE_ERROR;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.morak.back.appointment.domain.availabletime.datetimeperiod.AvailableTimeDateTimePeriod;
import com.morak.back.appointment.exception.AppointmentDomainLogicException;
import com.morak.back.core.domain.times.LocalTimes;
import com.morak.back.core.domain.times.Times;
import com.morak.back.core.exception.CustomErrorCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TimeConditionsTest {
    private final Times times = new LocalTimes();

    public static Stream<Arguments> invalidMinuteUnitTimePeriod() {
        LocalDateTime now = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.of(9, 0));
        // now = 현재 날짜, 오전 9시

        return Stream.of(
                Arguments.of(AvailableTimeDateTimePeriod.of(now.plusDays(1), now.plusDays(2).plusMinutes(30), now),
                        AVAILABLETIME_DURATION_NOT_MINUTES_UNIT_ERROR),
                // 날짜가 하루 차이 나면서 30분 차이나는 경우

                Arguments.of(AvailableTimeDateTimePeriod.of(now.plusDays(1), now.plusDays(1).plusMinutes(40), now),
                        AVAILABLETIME_DURATION_NOT_MINUTES_UNIT_ERROR),
                //40분 차이가 나는 경우

                Arguments.of(AvailableTimeDateTimePeriod.of(now.plusDays(1), now.plusDays(1).plusMinutes(29),
                        now), AVAILABLETIME_DURATION_NOT_MINUTES_UNIT_ERROR),
                // 29분 차이나는 경우

                Arguments.of(
                        AvailableTimeDateTimePeriod.of(now.plusDays(1).plusMinutes(20), now.plusDays(1).plusMinutes(50),
                                now), AVAILABLETIME_NOT_DIVIDED_BY_MINUTES_UNIT_ERROR)
                // 30분 차이는 나지만, 30분 단위로 안 나눠지는 경우
        );
    }

    public static Stream<Arguments> invalidRangeDateTimePeriod() {
        LocalDateTime now = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.of(9, 0));
        // now = 현재 날짜, 오전 9시

        return Stream.of(
                Arguments.of(AvailableTimeDateTimePeriod.of(now.minusDays(1), now.minusDays(1).plusMinutes(30),
                        now.minusDays(2))),
                // 시작, 끝 날짜가 약속 선택 범위보다 이전 인경우

                Arguments.of(AvailableTimeDateTimePeriod.of(now.plusDays(11), now.plusDays(11).plusMinutes(30),
                        now.minusDays(2)))
                // 시작, 끝 날짜가 약속 선택 범위보다 아후 인경우
        );
    }

    public static Stream<Arguments> invalidRangeTimePeriod() {
        LocalDateTime now = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.of(9, 0));
        // now = 현재 날짜, 오전 9시

        return Stream.of(
                Arguments.of(AvailableTimeDateTimePeriod.of(now.plusDays(1).plusHours(2),
                        now.plusDays(1).plusHours(2).plusMinutes(30), now)),
                // 시작 시간이 등록된 약속 잡기의 끝 시간과 같은 경우

                Arguments.of(AvailableTimeDateTimePeriod.of(now.plusDays(10).plusHours(2),
                        now.plusDays(10).plusHours(2).plusMinutes(30), now)),
                // 시작, 끝 시간이 약속 선택 범위보다 아후 인경우

                Arguments.of(AvailableTimeDateTimePeriod.of(now.plusDays(1).minusMinutes(30),
                        now.plusDays(1), now.minusDays(2)))
                // 시작 시간이 등록된 약속 잡기보다 이전 시간인 경우
        );
    }

    public static Stream<Arguments> midNightFailTest() {
        LocalDateTime now = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.of(23, 0));
        // now = 현재 날짜, 23시

        return Stream.of(
                Arguments.of(AvailableTimeDateTimePeriod.of(now.plusDays(10).plusHours(1), now.plusDays(10).plusMinutes(90), now)),
                Arguments.of(AvailableTimeDateTimePeriod.of(now.plusDays(9).plusHours(1), now.plusDays(9).plusMinutes(90), now)),
                Arguments.of(AvailableTimeDateTimePeriod.of(now.plusDays(1).plusHours(1), now.plusDays(1).plusMinutes(90), now))
                // 새벽 12시 ~ 12시 30분 선택한 경우
        );
    }

    public static Stream<Arguments> midNightSuccessTest() {
        LocalDateTime now = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.of(23, 0));
        // now = 현재 날짜, 23시

        return Stream.of(
                Arguments.of(AvailableTimeDateTimePeriod.of(now.plusDays(10).plusMinutes(30), now.plusDays(10).plusHours(1), now)),
                Arguments.of(AvailableTimeDateTimePeriod.of(now.plusDays(9).plusMinutes(30), now.plusDays(9).plusHours(1), now)),
                Arguments.of(AvailableTimeDateTimePeriod.of(now.plusDays(1).plusMinutes(30), now.plusDays(1).plusHours(1), now))
                // 밤 11시 반 ~ 00시 선택한 경우
        );
    }

    @Test
    void TimeCondition을_정상적으로_생성_가능하다() {
        // given
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.plusDays(1);
        LocalDate endDate = now.plusDays(10);

        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(11, 0);

        int durationHours = 1;
        int durationMinutes = 0;

        // when & then
        assertThatCode(() -> TimeConditions.of(startDate, endDate,
                startTime, endTime,
                durationHours, durationMinutes,
                times.dateOfNow()))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("invalidMinuteUnitTimePeriod")
    void DateTimePeriod가_30분_단위와_맞지_않는_경우_예외를_던진다(AvailableTimeDateTimePeriod dateTimePeriod,
                                                  CustomErrorCode customErrorCode) {

        // given
        LocalDateTime nowDateTime = LocalDateTime.now();
        LocalDate nowDate = nowDateTime.toLocalDate();

        LocalDate startDate = nowDate.plusDays(1);
        LocalDate endDate = nowDate.plusDays(10);

        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(11, 0);

        int durationHours = 1;
        int durationMinutes = 0;

        TimeConditions timeConditions = TimeConditions.of(startDate, endDate,
                startTime, endTime,
                durationHours, durationMinutes,
                times.dateOfNow());

        // when & then
        assertThatThrownBy(() -> timeConditions.validateDateTimePeriod(dateTimePeriod))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(customErrorCode);
    }

    @ParameterizedTest
    @MethodSource("invalidRangeDateTimePeriod")
    void DateTimePeriod가_약속잡기_날짜_범위에_들어오지_않는_경우_예외를_던진다(AvailableTimeDateTimePeriod dateTimePeriod) {

        // given
        LocalDateTime nowDateTime = LocalDateTime.now();
        LocalDate nowDate = nowDateTime.toLocalDate();

        LocalDate startDate = nowDate.plusDays(1);
        LocalDate endDate = nowDate.plusDays(10);

        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(11, 0);

        int durationHours = 1;
        int durationMinutes = 0;

        TimeConditions timeConditions = TimeConditions.of(startDate, endDate,
                startTime, endTime,
                durationHours, durationMinutes,
                times.dateOfNow());

        // when & then
        assertThatThrownBy(() -> timeConditions.validateDateTimePeriod(dateTimePeriod))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(AVAILABLETIME_DATE_OUT_OF_RANGE_ERROR);
    }

    @ParameterizedTest
    @MethodSource("invalidRangeTimePeriod")
    void DateTimePeriod가_약속잡기_시간_범위에_들어오지_않는_경우_예외를_던진다(AvailableTimeDateTimePeriod dateTimePeriod) {

        // given
        LocalDateTime nowDateTime = LocalDateTime.now();
        LocalDate nowDate = nowDateTime.toLocalDate();

        LocalDate startDate = nowDate.plusDays(1);
        LocalDate endDate = nowDate.plusDays(10);

        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(11, 0);

        int durationHours = 1;
        int durationMinutes = 0;

        TimeConditions timeConditions = TimeConditions.of(startDate, endDate,
                startTime, endTime,
                durationHours, durationMinutes,
                times.dateOfNow());

        // when & then
        assertThatThrownBy(() -> timeConditions.validateDateTimePeriod(dateTimePeriod))
                .isInstanceOf(AppointmentDomainLogicException.class)
                .extracting("code")
                .isEqualTo(AVAILABLETIME_TIME_OUT_OF_RANGE_ERROR);
    }


    @ParameterizedTest
    @MethodSource("midNightFailTest")
    void 약속잡기의_가능_끝_시간이_MIDNIGHT인_경우에_대해_범위를_넘어가는_경우_예외를_던진다(AvailableTimeDateTimePeriod dateTimePeriod) {

        // given
        LocalDateTime nowDateTime = LocalDateTime.now();
        LocalDate nowDate = nowDateTime.toLocalDate();

        LocalDate startDate = nowDate.plusDays(1);
        LocalDate endDate = nowDate.plusDays(10);

        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(0, 0);

        int durationHours = 1;
        int durationMinutes = 0;

        TimeConditions timeConditions = TimeConditions.of(startDate, endDate,
                startTime, endTime,
                durationHours, durationMinutes,
                times.dateOfNow());

        // when & then
        assertThatThrownBy(() -> timeConditions.validateDateTimePeriod(dateTimePeriod))
                .isInstanceOf(AppointmentDomainLogicException.class);
    }

    @ParameterizedTest
    @MethodSource("midNightSuccessTest")
    void 약속잡기의_가능_끝_시간이_MIDNIGHT인_경우에_대해_다양한_성공_테스트(AvailableTimeDateTimePeriod dateTimePeriod) {

        // given
        LocalDateTime nowDateTime = LocalDateTime.now();
        LocalDate nowDate = nowDateTime.toLocalDate();

        LocalDate startDate = nowDate.plusDays(1);
        LocalDate endDate = nowDate.plusDays(10);

        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(0, 0);

        int durationHours = 1;
        int durationMinutes = 0;

        TimeConditions timeConditions = TimeConditions.of(startDate, endDate,
                startTime, endTime,
                durationHours, durationMinutes,
                times.dateOfNow());

        // when & then
        assertThatCode(() -> timeConditions.validateDateTimePeriod(dateTimePeriod))
                .doesNotThrowAnyException();
    }
}

