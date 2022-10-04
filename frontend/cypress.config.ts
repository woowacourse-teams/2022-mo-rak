import { defineConfig } from 'cypress';

export default defineConfig({
  projectId: 'vs3n5w',
  e2e: {},
  viewportHeight: 1080,
  viewportWidth: 1920,
  env: {
    'cypress-react-selector': {
      root: '#root'
    }
  }
});
