import { Global, css } from '@emotion/react';
import theme from './theme';

const style = css`
  @font-face {
    font-family: 'TmoneyRoundWindRegular';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_20-07@1.0/TmoneyRoundWindRegular.woff') format('woff');
    font-weight: normal;
    font-style: normal;
    font-display: swap;
  }

  html, body, div, span, applet, object, iframe,
  h1, h2, h3, h4, h5, h6, p, blockquote, pre,
  a, abbr, acronym, address, big, cite, code,
  del, dfn, em, img, ins, kbd, q, s, samp,
  small, strike, strong, sub, sup, tt, var,
  b, u, i, center,
  dl, dt, dd, ol, ul, li,
  fieldset, form, label, legend,
  table, caption, tbody, tfoot, thead, tr, th, td,
  article, aside, canvas, details, embed, 
  figure, figcaption, footer, header, hgroup, 
  menu, nav, output, ruby, section, summary,
  time, mark, audio, video, input {
    margin: 0;
    padding: 0;
    border: 0;
    font-size: 100%;
    font: inherit;
    vertical-align: baseline;
    box-sizing: border-box;
  }
  
  html {
    font-size: 10px;
    font-family: 'TmoneyRoundWindRegular', sans-serif;
  }


  article, aside, details, figcaption, figure, 
  footer, header, hgroup, menu, nav, section {
    display: block;
  }

  body {
    line-height: 1;
    background-color: ${theme.colors.GRAY_100};
  }

  ol, ul {
    list-style: none;
  }

  blockquote, q {
    quotes: none;
  }

  blockquote:before, blockquote:after,
  q:before, q:after {
    content: '';
    content: none;
  }
  
  table {
    border-collapse: collapse;
    border-spacing: 0;
  }

  button {
    border: none;
    background: transparent;
    cursor: pointer;
    font: inherit;
  }

  input:focus {
    outline: none;
  }

  @media screen and (max-width: 1919px) {
    html {
      font-size: 8px;
    }
  }

  @media screen and (max-width: 1366px) {
    html {
      font-size: 7px;
    }
  }

  @media screen and (max-width: 700px) {
    html {
      font-size: 4px;
    }
  }
`

function GlobalStyle() {
  return <Global styles={style} />;
}

export default GlobalStyle;
