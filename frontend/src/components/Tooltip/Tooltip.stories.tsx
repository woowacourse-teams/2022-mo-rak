import { Meta, Story } from '@storybook/react';
import Tooltip from './Tooltip';
import styled from '@emotion/styled';
import theme from '../../styles/theme';

export default {
  title: 'Reusable Components/Tooltip',
  component: Tooltip
} as Meta;

const SampleBox = styled.div`
  width: 10rem;
  height: 10rem;
  border: 1px solid ${theme.colors.BLACK_100};
  display: inline-block;
`;

const CenteredContainer = styled.div`
  display: flex;
  height: 100vh;
  justify-content: center;
  align-items: center;
`;

const DefaultTemplate: Story = (args) => (
  <CenteredContainer>
    <Tooltip placement="top" content="툴팁입니다." width="8" {...args}>
      <SampleBox />
    </Tooltip>
  </CenteredContainer>
);

const Default = DefaultTemplate.bind({});

export { Default };
