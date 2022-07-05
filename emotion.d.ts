import '@emotion/react';
import theme from './src/styles/theme';

declare module '@emotion/react' {
  export interface Theme {
    colors: typeof theme.colors;
  }
}
