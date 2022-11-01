import styled from '@emotion/styled';
import { Meta, Story } from '@storybook/react';
import theme from '../../styles/theme';
import MarginContainer from './MarginContainer';

export default {
  title: 'Reusable Components/MarginContainer',
  component: MarginContainer
} as Meta;

const SampleBox = styled.div`
  width: 10rem;
  height: 10rem;
  border: 1px solid ${theme.colors.BLACK_100};
`;
const DefaultTemplate: Story = (args) => (
  <div>
    <SampleBox />
    <MarginContainer margin="4rem 0" {...args}>
      <SampleBox />
    </MarginContainer>
    <SampleBox />
  </div>
);

const Default = DefaultTemplate.bind({});

export { Default };
