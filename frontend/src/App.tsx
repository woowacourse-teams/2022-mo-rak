import { Route, Routes } from 'react-router-dom';
import { lazy, Suspense } from 'react';
import { PrivateRoute } from '@/routes/PrivateRoute';
import { MenuRoute } from '@/routes/MenuRoute';
import NavigationLayout from '@/layouts/NavigationLayout/NavigationLayout';
import GroupRoute from '@/routes/GroupRoute';
import ErrorPage from '@/pages/ErrorPage/ErrorPage';
import { AuthProvider } from '@/contexts/AuthProvider';
import FallbackPage from '@/pages/FallbackPage/FallbackPage';
import * as Sentry from '@sentry/react';

const PollMainPage = lazy(() => import('@/pages/PollMainPage/PollMainPage'));
const PollCreatePage = lazy(() => import('@/pages/PollCreatePage/PollCreatePage'));
const PollProgressPage = lazy(() => import('@/pages/PollProgressPage/PollProgressPage'));
const LandingPage = lazy(() => import('@/pages/LandingPage/LandingPage'));
const PollResultPage = lazy(() => import('@/pages/PollResultPage/PollResultPage'));
const GroupInitPage = lazy(() => import('@/pages/GroupInitPage/GroupInitPage'));
const MainPage = lazy(() => import('@/pages/MainPage/MainPage'));
const InvitationPage = lazy(() => import('@/pages/InvitationPage/InvitationPage'));
const AppointmentMainPage = lazy(() => import('@/pages/AppointmentMainPage/AppointmentMainPage'));
const AppointmentCreatePage = lazy(
  () => import('@/pages/AppointmentCreatePage/AppointmentCreatePage')
);
const AppointmentProgressPage = lazy(
  () => import('@/pages/AppointmentProgressPage/AppointmentProgressPage')
);
const AppointmentResultPage = lazy(
  () => import('@/pages/AppointmentResultPage/AppointmentResultPage')
);
const RoleMainPage = lazy(() => import('@/pages/RoleMainPage/RoleMainPage'));

if (process.env.NODE_ENV === 'production') {
  Sentry.init({
    dsn: process.env.SENTRY_DSN,
    integrations: [
      new Sentry.BrowserTracing({
        tracePropagationTargets: [/^https:\/\/mo-rak\.com\/api/]
      }),
      new Sentry.Replay()
    ],
    replaysSessionSampleRate: 0.1,
    replaysOnErrorSampleRate: 1.0,
    tracesSampleRate: 1.0
  });
}

function App() {
  return (
    <Suspense fallback={<FallbackPage />}>
      <Routes>
        <Route path="/">
          <Route index element={<LandingPage />} />
          <Route path="invite/:invitationCode" element={<InvitationPage />} />
          <Route
            element={
              <AuthProvider>
                <PrivateRoute />
              </AuthProvider>
            }
          >
            <Route path="init" element={<GroupInitPage />} />

            <Route element={<NavigationLayout />}>
              <Route path="groups/:groupCode">
                <Route element={<GroupRoute />}>
                  <Route element={<MenuRoute menu={null} />}>
                    <Route index element={<MainPage />} />
                  </Route>

                  <Route element={<MenuRoute menu="poll" />}>
                    <Route path="poll">
                      <Route index element={<PollMainPage />} />
                      <Route path="create" element={<PollCreatePage />} />
                      <Route path=":pollCode/progress" element={<PollProgressPage />} />
                      <Route path=":pollCode/result" element={<PollResultPage />} />
                    </Route>
                  </Route>

                  <Route element={<MenuRoute menu="appointment" />}>
                    <Route path="appointment">
                      <Route index element={<AppointmentMainPage />} />
                      <Route path="create" element={<AppointmentCreatePage />} />
                      <Route
                        path=":appointmentCode/progress"
                        element={<AppointmentProgressPage />}
                      />
                      <Route path=":appointmentCode/result" element={<AppointmentResultPage />} />
                    </Route>
                  </Route>

                  <Route element={<MenuRoute menu="role" />}>
                    <Route path="role">
                      <Route index element={<RoleMainPage />} />
                    </Route>
                  </Route>
                </Route>
              </Route>
            </Route>
          </Route>

          <Route path="*" element={<ErrorPage />} />
        </Route>
      </Routes>
    </Suspense>
  );
}

export default App;
