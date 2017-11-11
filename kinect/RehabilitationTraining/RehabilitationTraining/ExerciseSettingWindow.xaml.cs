using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace RehabilitationTraining
{
    /// <summary>
    /// Window1.xaml 的交互逻辑
    /// </summary>
    public partial class ExerciseSettingWindow : Window
    {
        public ExerciseSettingWindow()
        {
            InitializeComponent();
        }

        private void button_Click(object sender, RoutedEventArgs e)
        {

        }

        private void listBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (((sender as ListBox).SelectedItem as RadioButton).Name == "item_Neck")
            {
                MessageBox.Show("Test");
            }
        }
    }
}
